package com.example.contentproviderconsumidor.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Define o "contrato" entre o Content Provider de mensagens e as aplicações que o consomem.
 * Um contrato é uma classe que contém constantes para as URIs, nomes de tabelas, e nomes de colunas.
 * Isso garante que o provedor e os consumidores usem as mesmas strings, evitando erros de digitação.
 */
public final class MensagemContract {

    /**
     * O construtor privado impede que a classe do contrato seja instanciada.
     * Esta classe deve ser usada apenas para acessar suas constantes estáticas.
     */
    private MensagemContract() {}

    /**
     * A autoridade do Content Provider. É uma string única que identifica o provedor
     * no sistema Android. Geralmente, é o nome do pacote da aplicação que o provê.
     */
    public static final String AUTHORITY = "com.example.contentprovidergerador.provider";

    /**
     * A URI base de conteúdo (content://) que será usada para interagir com o Content Provider.
     * Todas as outras URIs específicas de tabelas serão construídas a partir desta.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * O caminho (path) para o diretório de "mensagens". Este valor é anexado à URI base
     * para formar a URI de acesso à tabela de mensagens.
     */
    public static final String PATH_MENSAGENS = "mensagens";

    /**
     * Classe interna que define as constantes para a tabela de mensagens.
     * Implementar a interface {@link BaseColumns} adiciona automaticamente as colunas
     * {@code _ID} e {@code _COUNT}, que são convenções padrão do Android, especialmente
     * úteis ao usar um CursorAdapter.
     */
    public static final class MensagemEntry implements BaseColumns {

        /**
         * A URI de conteúdo completa para acessar os dados da tabela de mensagens.
         * É construída a partir da URI base e do caminho da tabela.
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MENSAGENS).build();

        // Nome da tabela no banco de dados.
        public static final String TABELA = "mensagens";

        // Nome da coluna para o texto da mensagem.
        public static final String COLUNA_TEXTO = "texto";

        // Nome da coluna para o autor da mensagem.
        public static final String COLUNA_AUTOR = "autor";

        // Nome da coluna que indica se a mensagem é favorita.
        // É armazenado como um INTEGER, onde 1 significa 'verdadeiro' (favorita) e 0 'falso'.
        public static final String COLUNA_FAVORITA = "favorita";
    }
}