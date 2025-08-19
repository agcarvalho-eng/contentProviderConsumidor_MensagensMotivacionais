package com.example.contentproviderconsumidor.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.contentproviderconsumidor.R;
import com.google.android.material.navigation.NavigationView;

/**
 * A Activity principal e única do aplicativo, seguindo o padrão de "Single-Activity Architecture".
 * Sua responsabilidade é hospedar o contêiner de navegação (NavHostFragment) e configurar
 * os componentes de UI globais, como a Toolbar (barra de ferramentas superior) e a
 * NavigationView (menu de navegação lateral).
 */
public class MainActivity extends AppCompatActivity {

    /** Configuração que conecta a ActionBar/Toolbar com o NavController, definindo os destinos de nível superior. */
    private AppBarConfiguration appBarConfiguration;

    /** O controlador central que gerencia a navegação entre os fragments dentro do NavHost. */
    private NavController navController;

    /**
     * Chamado quando a activity é criada pela primeira vez.
     * Este método é responsável por inflar o layout e configurar toda a estrutura de navegação.
     *
     * @param savedInstanceState Se a activity estiver sendo recriada, este Bundle contém
     * os dados mais recentes fornecidos em onSaveInstanceState().
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Passo 1: Configurar a Toolbar para ser a ActionBar oficial desta Activity.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Passo 2: Encontrar os componentes essenciais para a navegação.
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        // Encontra o NavHostFragment, que é o contêiner onde os fragments serão exibidos.
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        // Obtém o NavController a partir do NavHostFragment.
        navController = navHostFragment.getNavController();

        // Passo 3: Configurar a AppBarConfiguration.
        // Define quais destinos são considerados "de nível superior". Nesses destinos,
        // a Toolbar exibirá o ícone de menu (hambúrguer) em vez de uma seta de "voltar".
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragmentoExibirMensagem)
                .setOpenableLayout(drawerLayout) // Associa a configuração ao nosso DrawerLayout.
                .build();

        // Passo 4: Conectar a ActionBar (Toolbar) com o NavController.
        // Isso fará com que o título da Toolbar seja atualizado automaticamente e
        // o botão de menu/voltar seja exibido e funcione corretamente.
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Passo 5: Conectar a NavigationView (o menu lateral) com o NavController.
        // Isso permite que, ao clicar em um item do menu, o NavController navegue
        // para o destino correspondente automaticamente.
        NavigationUI.setupWithNavController(navView, navController);
    }

    /**
     * Sobrescrito para delegar o evento de clique no botão "Up" (a seta de voltar na Toolbar)
     * para o NavController. Sem isso, a navegação de volta pela Toolbar não funcionaria.
     *
     * @return true se a navegação "Up" foi bem-sucedida, false caso contrário.
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Tenta navegar para cima na pilha de navegação. Se não for possível, recorre ao comportamento padrão.
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}