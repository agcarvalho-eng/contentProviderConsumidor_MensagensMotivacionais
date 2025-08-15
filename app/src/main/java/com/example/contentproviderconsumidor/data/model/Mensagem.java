package com.example.contentproviderconsumidor.data.model;

/**
 * Representa o objeto de modelo (Model) para uma única mensagem.
 * Esta classe é um POJO (Plain Old Java Object) que encapsula os dados de uma mensagem,
 * facilitando a transferência de dados entre a camada de dados (Content Provider) e a UI.
 * Sua estrutura espelha as colunas definidas em {@link com.example.contentproviderconsumidor.data.db.MensagemContract.MensagemEntry}.
 */
public class Mensagem {

    /** O identificador único da mensagem, correspondente à coluna _ID no banco de dados. */
    private long id;

    /** O conteúdo textual da mensagem. */
    private String texto;

    /** O nome do autor da mensagem. */
    private String autor;

    /**
     * Um indicador de status de favorito.
     * Utiliza a convenção de 1 para 'favorita' e 0 para 'não favorita'.
     */
    private int favorita;

    /**
     * Construtor padrão (vazio).
     * É necessário para certas bibliotecas e frameworks que podem precisar instanciar
     * o objeto via reflexão (reflection), como algumas ferramentas de ORM ou serialização.
     */
    public Mensagem() {}

    /**
     * Construtor completo para criar uma instância de Mensagem com todos os seus dados.
     * É útil para popular o objeto diretamente a partir de uma fonte de dados, como um Cursor.
     *
     * @param id       O ID único da mensagem.
     * @param texto    O conteúdo da mensagem.
     * @param autor    O autor da mensagem.
     * @param favorita O status de favorita (1 para sim, 0 para não).
     */
    public Mensagem(long id, String texto, String autor, int favorita) {
        this.id = id;
        this.texto = texto;
        this.autor = autor;
        this.favorita = favorita;
    }

    // --- Getters e Setters ---

    /**
     * Retorna o ID da mensagem.
     * @return O ID único do tipo long.
     */
    public long getId() {
        return id;
    }

    /**
     * Define o ID da mensagem.
     * @param id O novo ID único para a mensagem.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retorna o texto da mensagem.
     * @return A String contendo o texto da mensagem.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Define o texto da mensagem.
     * @param texto O novo conteúdo textual para a mensagem.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Retorna o autor da mensagem.
     * @return A String contendo o nome do autor.
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Define o autor da mensagem.
     * @param autor O novo nome do autor para a mensagem.
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
     * Retorna o status de favorita da mensagem.
     * @return Um inteiro: 1 se for favorita, 0 caso contrário.
     */
    public int getFavorita() {
        return favorita;
    }

    /**
     * Define o status de favorita da mensagem.
     * @param favorita O novo status, que deve ser 1 (favorita) ou 0 (não favorita).
     */
    public void setFavorita(int favorita) {
        this.favorita = favorita;
    }
}
