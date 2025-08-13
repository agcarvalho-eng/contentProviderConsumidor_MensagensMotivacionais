package com.example.contentproviderconsumidor.data.model;

/**
 * Esta é a classe de Modelo (Model) que representa uma mensagem.
 * É uma cópia idêntica da classe de modelo do app Gerador,
 * garantindo que ambos os apps "entendam" a estrutura dos dados da mesma forma.
 */
public class Mensagem {
    private long id;
    private String texto;
    private String autor;
    private int favorita; // 1 para favorita, 0 para não favorita

    // Construtor vazio necessário para algumas operações
    public Mensagem() {}

    // Construtor principal para criar objetos a partir dos dados do Cursor
    public Mensagem(long id, String texto, String autor, int favorita) {
        this.id = id;
        this.texto = texto;
        this.autor = autor;
        this.favorita = favorita;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getFavorita() {
        return favorita;
    }

    public void setFavorita(int favorita) {
        this.favorita = favorita;
    }
}
