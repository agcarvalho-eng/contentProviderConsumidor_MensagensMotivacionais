package com.example.contentproviderconsumidor.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contentproviderconsumidor.databinding.FragmentoListarFavoritasBinding;
import com.example.contentproviderconsumidor.ui.adapter.MensagemFavoritaAdapter;
import com.example.contentproviderconsumidor.ui.viewmodel.MensagemConsumidorViewModel;

/**
 * Um Fragment que exibe uma lista de todas as mensagens marcadas como favoritas.
 * Ele utiliza um RecyclerView para exibir a lista de forma eficiente.
 * Assim como outros fragments neste app, ele se comunica com um {@link MensagemConsumidorViewModel}
 * compartilhado para obter os dados.
 */
public class FragmentoListarFavoritas extends Fragment {

    /** Instância do View Binding para este fragmento, para acesso seguro às views. */
    private FragmentoListarFavoritasBinding binding;

    /**
     * A mesma instância de ViewModel usada por outros fragments na Activity.
     * Serve como a única fonte da verdade para os dados de mensagens.
     */
    private MensagemConsumidorViewModel viewModel;

    /** O Adapter responsável por vincular a lista de mensagens favoritas ao RecyclerView. */
    private MensagemFavoritaAdapter adapter;

    /**
     * Chamado para o fragmento instanciar sua hierarquia de views.
     *
     * @param inflater O LayoutInflater para inflar as views no fragmento.
     * @param container A view pai à qual a UI do fragmento deve ser anexada.
     * @param savedInstanceState Se não nulo, o fragmento está sendo reconstruído a partir de um estado salvo.
     * @return A View raiz para a UI do fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentoListarFavoritasBinding.inflate(inflater, container, false);

        // Ponto chave da arquitetura: obtém a instância do ViewModel com escopo da Activity.
        // Isso garante que os dados sejam consistentes entre os fragments.
        viewModel = new ViewModelProvider(requireActivity()).get(MensagemConsumidorViewModel.class);

        return binding.getRoot();
    }

    /**
     * Chamado após a view do fragmento ter sido criada. Ideal para configurar
     * componentes de UI como o RecyclerView e iniciar a observação de dados.
     *
     * @param view A View retornada por onCreateView().
     * @param savedInstanceState Se não nulo, o fragmento está sendo reconstruído.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        observeViewModel();
    }

    /**
     * Configura o RecyclerView, incluindo seu LayoutManager e Adapter.
     * Prepara a lista para receber e exibir os dados.
     */
    private void setupRecyclerView() {
        adapter = new MensagemFavoritaAdapter();
        binding.recyclerViewFavoritas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewFavoritas.setAdapter(adapter);
    }

    /**
     * Configura o observador no LiveData de mensagens favoritas do ViewModel.
     * A UI reagirá automaticamente a quaisquer alterações na lista de favoritos
     * (adição ou remoção de itens).
     */
    private void observeViewModel() {
        // Observa o LiveData que contém a lista de mensagens favoritas.
        // O código dentro do lambda será executado sempre que a lista for atualizada.
        viewModel.getMensagensFavoritas().observe(getViewLifecycleOwner(), favoritas -> {
            // Verifica se a lista de favoritos está vazia.
            if (favoritas != null && favoritas.isEmpty()) {
                // Se estiver vazia, exibe uma mensagem informativa e oculta o RecyclerView.
                binding.tvListaVazia.setVisibility(View.VISIBLE);
                binding.recyclerViewFavoritas.setVisibility(View.GONE);
            } else {
                // Se houver favoritos, oculta a mensagem de lista vazia e exibe o RecyclerView.
                binding.tvListaVazia.setVisibility(View.GONE);
                binding.recyclerViewFavoritas.setVisibility(View.VISIBLE);
                // Atualiza o adapter com a nova lista de mensagens favoritas.
                adapter.setMensagens(favoritas);
            }
        });
    }

    /**
     * Chamado quando a hierarquia de views do fragmento está sendo destruída.
     * Limpa a referência ao binding para prevenir vazamentos de memória.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}