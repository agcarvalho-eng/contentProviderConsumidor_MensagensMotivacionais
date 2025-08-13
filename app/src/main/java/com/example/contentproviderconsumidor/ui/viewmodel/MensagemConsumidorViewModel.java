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

public class MensagemConsumidorViewModel extends AndroidViewModel {

    private MutableLiveData<Mensagem> mensagemAleatoria = new MutableLiveData<>();
    private MutableLiveData<List<Mensagem>> mensagensFavoritas = new MutableLiveData<>();
    private static long ultimaMensagemId = -1;

    public MensagemConsumidorViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Mensagem> getMensagemAleatoria() {
        carregarMensagemAleatoria();
        return mensagemAleatoria;
    }

    public LiveData<List<Mensagem>> getMensagensFavoritas() {
        carregarMensagensFavoritas();
        return mensagensFavoritas;
    }

    private void carregarMensagemAleatoria() {
        List<Mensagem> todasAsMensagens = buscarTodas();
        if (todasAsMensagens == null || todasAsMensagens.isEmpty()) {
            mensagemAleatoria.setValue(null);
            return;
        }

        List<Mensagem> pool = new ArrayList<>();
        for (Mensagem m : todasAsMensagens) {
            if (m.getId() != ultimaMensagemId) {
                pool.add(m);
            }
        }

        if (pool.isEmpty()) { // Caso só exista uma mensagem no total
            pool.addAll(todasAsMensagens);
        }

        Random random = new Random();
        Mensagem msgEscolhida = pool.get(random.nextInt(pool.size()));
        ultimaMensagemId = msgEscolhida.getId();
        mensagemAleatoria.setValue(msgEscolhida);
    }

    public void carregarMensagensFavoritas() {
        String selection = MensagemContract.MensagemEntry.COLUMN_FAVORITA + " = ?";
        String[] selectionArgs = new String[]{"1"};
        Cursor cursor = getApplication().getContentResolver().query(
                MensagemContract.MensagemEntry.CONTENT_URI,
                null, selection, selectionArgs, null
        );
        mensagensFavoritas.setValue(cursorParaLista(cursor));
    }

    private List<Mensagem> buscarTodas() {
        Cursor cursor = getApplication().getContentResolver().query(
                MensagemContract.MensagemEntry.CONTENT_URI,
                null, null, null, null
        );
        return cursorParaLista(cursor);
    }

    public void atualizarStatusFavorita(long id, boolean isFavorita) {
        Uri uri = MensagemContract.MensagemEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        ContentValues values = new ContentValues();
        values.put(MensagemContract.MensagemEntry.COLUMN_FAVORITA, isFavorita ? 1 : 0);
        getApplication().getContentResolver().update(uri, values, null, null);
    }

    private List<Mensagem> cursorParaLista(Cursor cursor) {
        ArrayList<Mensagem> lista = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry._ID));
                String texto = cursor.getString(cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry.COLUMN_TEXTO));
                String autor = cursor.getString(cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry.COLUMN_AUTOR));
                int favorita = cursor.getInt(cursor.getColumnIndexOrThrow(MensagemContract.MensagemEntry.COLUMN_FAVORITA));
                lista.add(new Mensagem(id, texto, autor, favorita));
            }
            cursor.close();
        }
        return lista;
    }
}
