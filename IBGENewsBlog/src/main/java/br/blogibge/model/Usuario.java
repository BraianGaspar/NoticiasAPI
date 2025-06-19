package br.blogibge.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private final String nome;
    private List<Noticia> favoritos = new ArrayList<>();
    private List<Noticia> paraLerDepois = new ArrayList<>();
    private List<Noticia> lidas = new ArrayList<>();

    public Usuario(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<Noticia> getFavoritos() {
        return favoritos;
    }

    public List<Noticia> getParaLerDepois() {
        return paraLerDepois;
    }

    public List<Noticia> getLidas() {
        return lidas;
    }

    public void adicionarFavorito(Noticia noticia) {
        if (favoritos == null) {
            favoritos = new ArrayList<>();
        }
        if (noticia != null && !favoritos.contains(noticia)) {
            favoritos.add(noticia);
        }
    }


    public void adicionarParaLerDepois(Noticia noticia) {
      if (paraLerDepois == null) {
        paraLerDepois = new ArrayList<>();
      }
      if (noticia != null && !paraLerDepois.contains(noticia)) {
        paraLerDepois.add(noticia);
      }
    }

    public void adicionarLida(Noticia noticia) {
      if (lidas == null) {
        lidas = new ArrayList<>();
      }
      if (noticia != null && !lidas.contains(noticia)) {
        lidas.add(noticia);
      }
    }
    public void removerFavorito(Noticia noticia) {
      if (favoritos.contains(noticia)) {
        favoritos.remove(noticia);
      }
    }
    public void removerParaLerDepois(Noticia noticia) {
        if (lidas.contains(noticia)) {
          paraLerDepois.remove(noticia);
        }
    }


    public void removerLida(Noticia noticia) {
      if (lidas.contains(noticia)) {
        lidas.remove(noticia);
      }
    }

    public void limparLidas() {
        lidas.clear();
  }
}
