package br.blogibge.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Noticia {
    private final int id;
    private final String titulo;
    private final String introducao;
    private final LocalDate dataPublicacao;
    private final String link;
    private final String tipo;
    private boolean lida = false;

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Noticia(int id, String titulo, String introducao, String dataPublicacaoStr, String link, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.introducao = introducao;
        this.dataPublicacao = parseData(dataPublicacaoStr);
        this.link = link;
        this.tipo = tipo;
    }

    private LocalDate parseData(String dataStr) {
        // Tenta parsear no formato ISO padrão da API (yyyy-MM-dd)
        try {
            return LocalDate.parse(dataStr);
        } catch (Exception e) {
            // fallback: retorna null caso não consiga parsear
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIntroducao() {
        return introducao;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public String getDataPublicacaoFormatada() {
        if (dataPublicacao == null) return "Data inválida";
        return dataPublicacao.format(DISPLAY_FORMATTER);
    }

    public String getLink() {
        return link;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isLida() {
        return lida;
    }

    public void marcarComoLida() {
        this.lida = true;
    }

    @Override
    public String toString() {
        return "\nTítulo: " + titulo +
                "\nIntrodução: " + introducao +
                "\nData de Publicação: " + getDataPublicacaoFormatada() +
                "\nLink: " + link +
                "\nTipo: " + tipo +
                "\nFonte: IBGE\n" +
                (lida ? "[LIDA]" : "[NÃO LIDA]");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Noticia)) return false;
        Noticia noticia = (Noticia) o;
        return id == noticia.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
