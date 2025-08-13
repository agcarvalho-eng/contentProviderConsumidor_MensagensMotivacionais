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
import com.example.contentproviderconsumidor.ui.adapter.MensagemFavoritaAdapter; // Crie este adapter
import com.example.contentproviderconsumidor.ui.viewmodel.MensagemConsumidorViewModel;

public class FragmentoListarFavoritas extends Fragment {
    private FragmentoListarFavoritasBinding binding;
    private MensagemConsumidorViewModel viewModel;
    private MensagemFavoritaAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentoListarFavoritasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MensagemFavoritaAdapter();
        binding.recyclerViewFavoritas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewFavoritas.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MensagemConsumidorViewModel.class);
        viewModel.getMensagensFavoritas().observe(getViewLifecycleOwner(), favoritas -> {
            adapter.setMensagens(favoritas);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recarrega as favoritas caso o status tenha sido alterado na outra tela
        viewModel.carregarMensagensFavoritas();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
