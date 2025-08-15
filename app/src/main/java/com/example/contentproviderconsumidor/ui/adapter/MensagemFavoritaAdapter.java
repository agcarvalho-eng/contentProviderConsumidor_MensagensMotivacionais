package com.example.contentproviderconsumidor.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentproviderconsumidor.R;
import com.example.contentproviderconsumidor.data.model.Mensagem;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para o RecyclerView que exibe a lista de mensagens favoritas.
 * Este adapter é responsável por pegar uma lista de objetos {@link Mensagem} e
 * mapear seus dados para as views definidas no layout {@code R.layout.item_mensagem_favorita}.
 */
public class MensagemFavoritaAdapter extends RecyclerView.Adapter<MensagemFavoritaAdapter.FavoritaViewHolder> {

    /** A lista de mensagens que o adapter irá exibir. */
    private List<Mensagem> mensagens = new ArrayList<>();

    /**
     * Chamado quando o RecyclerView precisa de um novo {@link FavoritaViewHolder} para representar um item.
     * Este método infla o layout do item a partir do XML e retorna o holder.
     *
     * @param parent O ViewGroup no qual a nova View será adicionada após ser vinculada a uma posição do adaptador.
     * @param viewType O tipo de view do novo View.
     * @return Uma nova instância de FavoritaViewHolder que contém a View para o item.
     */
    @NonNull
    @Override
    public FavoritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout XML customizado para cada item da lista.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensagem_favorita, parent, false);
        return new FavoritaViewHolder(view);
    }

    /**
     * Chamado pelo RecyclerView para exibir os dados na posição especificada.
     * Este método obtém o objeto Mensagem da lista com base na posição e
     * vincula seus dados ao ViewHolder correspondente.
     *
     * @param holder O ViewHolder que deve ser atualizado para representar o conteúdo do item na posição dada.
     * @param position A posição do item no conjunto de dados do adaptador.
     */
    @Override
    public void onBindViewHolder(@NonNull FavoritaViewHolder holder, int position) {
        // Busca a mensagem na posição atual da lista.
        Mensagem mensagem = mensagens.get(position);
        // Chama o método 'bind' do ViewHolder para popular a view com os dados da mensagem.
        holder.bind(mensagem);
    }

    /**
     * Retorna o número total de itens no conjunto de dados mantido pelo adaptador.
     *
     * @return O número total de mensagens na lista.
     */
    @Override
    public int getItemCount() {
        return mensagens != null ? mensagens.size() : 0;
    }

    /**
     * Atualiza a lista de mensagens do adapter e notifica o RecyclerView sobre a mudança.
     * Este é o método principal para fornecer ou atualizar os dados da lista.
     *
     * @param novasMensagens A nova lista de mensagens a ser exibida.
     */
    public void setMensagens(List<Mensagem> novasMensagens) {
        this.mensagens = novasMensagens;
        // Notifica o RecyclerView que o conjunto de dados mudou.
        // Isso força a lista a ser redesenhada com os novos dados.
        notifyDataSetChanged();
    }

    /**
     * O ViewHolder que descreve a view de um item e seus metadados para o RecyclerView.
     * Ele armazena as referências das views (para evitar chamadas repetidas de findViewById)
     * e contém a lógica para popular essas views com os dados do item.
     */
    static class FavoritaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTexto;
        private final TextView tvAutor;

        /**
         * Construtor para o ViewHolder.
         *
         * @param itemView A view raiz do layout do item (inflada em onCreateViewHolder).
         */
        public FavoritaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontra e armazena as referências para as TextViews dentro do layout do item.
            tvTexto = itemView.findViewById(R.id.tvTextoFavorito);
            tvAutor = itemView.findViewById(R.id.tvAutorFavorito);
        }

        /**
         * Vincula (bind) os dados de um objeto {@link Mensagem} às views deste ViewHolder.
         *
         * @param mensagem O objeto Mensagem contendo os dados a serem exibidos.
         */
        public void bind(Mensagem mensagem) {
            tvTexto.setText("\"" + mensagem.getTexto() + "\"");
            tvAutor.setText("- " + mensagem.getAutor());
        }
    }
}