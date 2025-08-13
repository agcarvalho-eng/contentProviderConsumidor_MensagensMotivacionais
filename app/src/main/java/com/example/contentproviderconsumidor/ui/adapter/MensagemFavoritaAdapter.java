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

public class MensagemFavoritaAdapter extends RecyclerView.Adapter<MensagemFavoritaAdapter.FavoritaViewHolder> {

    private List<Mensagem> mensagens = new ArrayList<>();

    @NonNull
    @Override
    public FavoritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensagem_favorita, parent, false);
        return new FavoritaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritaViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        holder.bind(mensagem);
    }

    @Override
    public int getItemCount() {
        return mensagens != null ? mensagens.size() : 0;
    }

    public void setMensagens(List<Mensagem> novasMensagens) {
        this.mensagens = novasMensagens;
        // Notifica o RecyclerView que o conjunto de dados mudou, para que ele possa redesenhar a lista.
        notifyDataSetChanged();
    }


    /**
     * ViewHolder para os itens da lista de favoritas.
     */
    static class FavoritaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTexto;
        private final TextView tvAutor;

        public FavoritaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.tvTextoFavorito);
            tvAutor = itemView.findViewById(R.id.tvAutorFavorito);
        }

        public void bind(Mensagem mensagem) {
            tvTexto.setText("\"" + mensagem.getTexto() + "\"");
            tvAutor.setText("- " + mensagem.getAutor());
        }
    }
}
