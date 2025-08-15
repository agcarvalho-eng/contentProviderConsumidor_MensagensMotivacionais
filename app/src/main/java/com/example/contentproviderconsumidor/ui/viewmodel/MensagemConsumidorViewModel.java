package com.example.contentproviderconsumidor.ui.viewmodel;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contentproviderconsumidor.data.db.MensagemContract;
import com.example.contentproviderconsumidor.data.model.Mensagem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para a tela de consumo de mensagens.
 * Esta classe atua como a ponte entre a camada de dados (ContentProvider) e a UI (Fragments).
 * Ela é responsável por buscar, processar e expor os dados através de LiveData,
 * garantindo que a UI seja sempre um reflexo do estado atual dos dados.
 * <p>
 * Estende {@link AndroidViewModel} para poder acessar o Context da aplicação de forma segura,
 * necessário para usar o ContentResolver.
 */
public class MensagemConsumidorViewModel extends AndroidViewModel {

    /** LiveData que expõe a mensagem aleatória atual para a UI. */
    private final MutableLiveData<Mensagem> mensagemAleatoria = new MutableLiveData<>();

    /** LiveData que expõe a lista de mensagens favoritas para a UI. */
    private final MutableLiveData<List<Mensagem>> mensagensFavoritas = new MutableLiveData<>();

    /**
     * Executor que roda em uma única thread para realizar todas as operações de banco de dados
     * (via ContentResolver) fora da thread principal, evitando o congelamento da UI.
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Armazena o ID da última mensagem exibida para evitar repetições consecutivas
     * ao buscar uma nova mensagem aleatória.
     */
    private static long ultimaMensagemId = -1;

    /**
     * Construtor do ViewModel. Inicia o carregamento dos dados iniciais
     * assim que o ViewModel é criado pela primeira vez.
     *
     * @param application A instância da aplicação, fornecida pelo framework.
     */
    public MensagemConsumidorViewModel(@NonNull Application application) {
        super(application);
        // Carrega os dados iniciais para popular a UI assim que ela for criada.
        carregarMensagemAleatoria();
        carregarMensagensFavoritas();
    }

    // --- Getters para expor LiveData (somente leitura) para a UI ---

    /**
     * Retorna o LiveData que contém a mensagem aleatória.
     * Os Fragments podem observar este LiveData para atualizar a UI automaticamente.
     * @return um {@link LiveData} contendo a {@link Mensagem} aleatória.
     */
    public LiveData<Mensagem> getMensagemAleatoria() {
        return mensagemAleatoria;
    }

    /**
     * Retorna o LiveData que contém a lista de mensagens favoritas.
     * O Fragment de favoritos observa este LiveData para atualizar a lista.
     * @return um {@link LiveData} contendo a lista de {@link Mensagem} favoritas.
     */
    public LiveData<List<Mensagem>> getMensagensFavoritas() {
        return mensagensFavoritas;
    }

    // --- Métodos de Ação chamados pela UI ---

    /**
     * Inicia o processo de carregar uma nova mensagem aleatória.
     * Este método é chamado pela UI quando o usuário solicita uma nova mensagem.
     */
    public void carregarNovaMensagemAleatoria() {
        carregarMensagemAleatoria();
    }

    /**
     * Busca, em uma thread de fundo, a lista de mensagens favoritas do ContentProvider.
     */
    public void carregarMensagensFavoritas() {
        executorService.execute(() -> {
            String selection = MensagemContract.MensagemEntry.COLUNA_FAVORITA + " = ?";
            String[] selectionArgs = new String[]{"1"}; // "1" representa 'true'
            Cursor cursor = getApplication().getContentResolver().query(
                    MensagemContract.MensagemEntry.CONTENT_URI,
                    null, selection, selectionArgs, null
            );
            // Usa postValue para atualizar o LiveData a partir de uma thread de fundo.
            mensagensFavoritas.postValue(cursorParaLista(cursor));
        });
    }

    /**
     * Atualiza o status de "favorita" de uma mensagem específica no ContentProvider.
     * Após a atualização, recarrega a lista de favoritos para garantir que
     * todas as partes da UI (como o outro fragment) reflitam a mudança.
     *
     * @param id O ID da mensagem a ser atualizada.
     * @param isFavorita O novo status de favorita (true para favorita, false para não).
     */
    public void atualizarStatusFavorita(long id, boolean isFavorita) {
        executorService.execute(() -> {
            Uri uri = MensagemContract.MensagemEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            ContentValues values = new ContentValues();
            values.put(MensagemContract.MensagemEntry.COLUNA_FAVORITA, isFavorita ? 1 : 0);

            int rowsAffected = getApplication().getContentResolver().update(uri, values, null, null);

            // Se a atualização foi bem-sucedida, recarrega a lista de favoritas.
            // Isso acionará o observer no Fragment da lista, atualizando a UI automaticamente.
            if (rowsAffected > 0) {
                carregarMensagensFavoritas();
            }
        });
    }

    // --- Métodos Privados de Lógica Interna ---

    /**
     * Lógica principal para buscar e selecionar uma mensagem aleatória.
     * Garante que a mesma mensagem não seja exibida duas vezes seguidas, se possível.
     */
    private void carregarMensagemAleatoria() {
        executorService.execute(() -> {
            List<Mensagem> todasAsMensagens = buscarTodas();
            // Se não houver mensagens, notifica a UI com um valor nulo.
            if (todasAsMensagens == null || todasAsMensagens.isEmpty()) {
                mensagemAleatoria.postValue(null);
                return;
            }

            // Cria uma lista de mensagens candidatas, excluindo a última exibida.
            List<Mensagem> pool = new ArrayList<>();
            for (Mensagem m : todasAsMensagens) {
                if (m.getId() != ultimaMensagemId) {
                    pool.add(m);
                }
            }

            // Se o pool ficou vazio (só havia uma mensagem), re-popula com todas.
            if (pool.isEmpty()) {
                pool.addAll(todasAsMensagens);
            }

            // Seleciona uma mensagem aleatória da lista de candidatas.
            Random random = new Random();
            Mensagem msgEscolhida = pool.get(random.nextInt(pool.size()));
            ultimaMensagemId = msgEscolhida.getId();

            // Atualiza o LiveData com a nova mensagem.
            mensagemAleatoria.postValue(msgEscolhida);
        });
    }

    /**
     * Busca todas as mensagens disponíveis no ContentProvider.
     * @return Uma lista de objetos {@link Mensagem}.
     */
    private List<Mensagem> buscarTodas() {
        Cursor cursor = getApplication().getContentResolver().query(
                MensagemContract.MensagemEntry.CONTENT_URI,
                null, null, null, null
        );
        return cursorParaLista(cursor);
    }

    /**
     * Converte um objeto {@link Cursor} em uma lista de objetos {@link Mensagem}.
     * Este método encapsula a lógica de iteração do cursor e garante que ele seja fechado.
     *
     * @param cursor O Cursor retornado pela consulta ao ContentResolver.
     * @return Uma {@link ArrayList} de objetos {@link Mensagem}.
     */
    private List<Mensagem> cursorParaLista(Cursor cursor) {
        ArrayList<Mensagem> lista = new ArrayList<>();
        if (cursor != null) {
            try {
                // Obtém os índices das colunas uma única vez para otimização.
                int idIndex = cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry._ID);
                int textoIndex = cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry.COLUNA_TEXTO);
                int autorIndex = cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry.COLUNA_AUTOR);
                int favoritaIndex = cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry.COLUNA_FAVORITA);

                // Itera sobre todas as linhas do cursor.
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idIndex);
                    String texto = cursor.getString(textoIndex);
                    String autor = cursor.getString(autorIndex);
                    int favorita = cursor.getInt(favoritaIndex);
                    // Cria o objeto Model e o adiciona à lista.
                    lista.add(new Mensagem(id, texto, autor, favorita));
                }
            } finally {
                // Garante que o cursor seja fechado para liberar recursos.
                cursor.close();
            }
        }
        return lista;
    }
}