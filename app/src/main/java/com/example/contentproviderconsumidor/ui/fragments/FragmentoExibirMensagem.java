package com.example.contentproviderconsumidor.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.contentproviderconsumidor.data.model.Mensagem;
import com.example.contentproviderconsumidor.databinding.FragmentoExibirMensagemBinding;
import com.example.contentproviderconsumidor.ui.viewmodel.MensagemConsumidorViewModel;

/**
 * Um Fragment responsável por exibir uma única mensagem e permitir que o usuário
 * a marque ou desmarque como favorita.
 * Este Fragment utiliza a arquitetura recomendada pelo Android com:
 * - {@link FragmentoExibirMensagemBinding} para acesso seguro às views (View Binding).
 * - {@link MensagemConsumidorViewModel} para obter e manipular os dados, separando a lógica da UI.
 */
public class FragmentoExibirMensagem extends Fragment {

    /** Instância do View Binding para este fragmento, permitindo acesso direto e seguro às views. */
    private FragmentoExibirMensagemBinding binding;

    /**
     * Instância do ViewModel. É escopado na Activity para compartilhar dados
     * entre fragments e sobreviver a mudanças de configuração (como rotação de tela).
     */
    private MensagemConsumidorViewModel viewModel;

    /** Armazena a mensagem atualmente exibida na tela para fácil acesso, como em listeners. */
    private Mensagem mensagemAtual;

    /**
     * Chamado para que o fragmento instancie sua hierarquia de views.
     *
     * @param inflater O LayoutInflater usado para inflar as views no fragmento.
     * @param container Se não nulo, esta é a view pai à qual a UI do fragmento deve ser anexada.
     * @param savedInstanceState Se não nulo, este fragmento está sendo reconstruído a partir de um estado salvo.
     * @return Retorna a View raiz para a UI do fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla o layout usando View Binding e armazena a referência.
        binding = FragmentoExibirMensagemBinding.inflate(inflater, container, false);

        // Ponto chave da arquitetura: obtém a instância do ViewModel com escopo da Activity.
        // Garante que o ViewModel seja o mesmo para todos os fragments na mesma activity
        // e que seus dados persistam durante a recriação do fragment.
        viewModel = new ViewModelProvider(requireActivity()).get(MensagemConsumidorViewModel.class);

        return binding.getRoot();
    }

    /**
     * Chamado imediatamente após onCreateView() ter retornado, mas antes de qualquer estado salvo
     * ter sido restaurado na view. É o local ideal para configurar as views, observers e listeners.
     *
     * @param view A View retornada por onCreateView().
     * @param savedInstanceState Se não nulo, este fragmento está sendo reconstruído.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicia a observação dos dados do ViewModel.
        observeViewModel();
        // Configura os listeners para as interações do usuário.
        setupListeners();
    }

    /**
     * Configura o observador para o LiveData no ViewModel.
     * A UI é atualizada automaticamente sempre que os dados no ViewModel mudam.
     */
    private void observeViewModel() {
        viewModel.getMensagemAleatoria().observe(getViewLifecycleOwner(), mensagem -> {
            if (mensagem != null) {
                // Guarda a referência da mensagem atual.
                mensagemAtual = mensagem;
                // Popula as views com os dados da nova mensagem.
                binding.tvTextoMensagem.setText("\"" + mensagem.getTexto() + "\"");
                binding.tvAutorMensagem.setText("- " + mensagem.getAutor());
                binding.cbFavorita.setVisibility(View.VISIBLE);

                // Lógica para evitar trigger indesejado do listener.
                // Remove o listener temporariamente para não ser acionado ao definirmos o estado do checkbox.
                binding.cbFavorita.setOnCheckedChangeListener(null);
                // Define o estado do checkbox com base no dado do objeto Mensagem.
                binding.cbFavorita.setChecked(mensagem.getFavorita() == 1);
                // Recoloca o listener para capturar as interações do usuário novamente.
                setupListeners();

            } else {
                // Caso nenhuma mensagem seja encontrada, exibe um estado vazio/informativo.
                binding.tvTextoMensagem.setText("Nenhuma mensagem encontrada. Cadastre algumas no app Gerador.");
                binding.tvAutorMensagem.setText("");
                binding.cbFavorita.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Configura os listeners para os componentes de UI, como o CheckBox de favoritos.
     */
    private void setupListeners() {
        binding.cbFavorita.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // A condição buttonView.isPressed() é um truque para garantir que o listener
            // só execute a lógica quando a mudança for causada por um toque direto do usuário,
            // ignorando mudanças programáticas (como a que ocorre no observeViewModel).
            if (mensagemAtual != null && buttonView.isPressed()) {
                viewModel.atualizarStatusFavorita(mensagemAtual.getId(), isChecked);
                Toast.makeText(getContext(), isChecked ? "Adicionado aos favoritos!" : "Removido dos favoritos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Chamado quando a hierarquia de views associada ao fragmento está sendo removida.
     * É crucial limpar as referências às views aqui para evitar vazamentos de memória (memory leaks).
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpa a referência do binding para permitir que o Garbage Collector libere a memória
        // associada à hierarquia de views do fragmento.
        binding = null;
    }
}
