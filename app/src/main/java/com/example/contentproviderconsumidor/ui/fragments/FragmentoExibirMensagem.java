package com.example.contentproviderconsumidor.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.contentproviderconsumidor.data.model.Mensagem;
import com.example.contentproviderconsumidor.databinding.FragmentoExibirMensagemBinding;
import com.example.contentproviderconsumidor.ui.viewmodel.MensagemConsumidorViewModel;

public class FragmentoExibirMensagem extends Fragment {
    private FragmentoExibirMensagemBinding binding;
    private MensagemConsumidorViewModel viewModel;
    private Mensagem mensagemAtual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentoExibirMensagemBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MensagemConsumidorViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cbFavorita.setOnCheckedChangeListener(null); // Evitar trigger no setup

        viewModel.getMensagemAleatoria().observe(getViewLifecycleOwner(), mensagem -> {
            if (mensagem != null) {
                mensagemAtual = mensagem;
                binding.tvTextoMensagem.setText("\"" + mensagem.getTexto() + "\"");
                binding.tvAutorMensagem.setText("- " + mensagem.getAutor());
                binding.cbFavorita.setChecked(mensagem.getFavorita() == 1);
            } else {
                binding.tvTextoMensagem.setText("Nenhuma mensagem encontrada. Cadastre algumas no app Gerador.");
                binding.tvAutorMensagem.setText("");
                binding.cbFavorita.setVisibility(View.GONE);
            }
        });

        binding.cbFavorita.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mensagemAtual != null) {
                viewModel.atualizarStatusFavorita(mensagemAtual.getId(), isChecked);
                Toast.makeText(getContext(), "Status atualizado!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
