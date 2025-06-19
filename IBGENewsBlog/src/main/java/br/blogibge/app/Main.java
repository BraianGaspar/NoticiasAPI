package br.blogibge.app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.blogibge.model.Noticia;
import br.blogibge.model.Usuario;
import br.blogibge.service.IBGENewsService;
import br.blogibge.util.JsonManager;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static IBGENewsService service = new IBGENewsService();
    static Usuario usuario;

    private static final DateTimeFormatter INPUT_FORMAT_1 = DateTimeFormatter.ofPattern("yyyyddMM");
    private static final DateTimeFormatter INPUT_FORMAT_2 = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter INPUT_FORMAT_3 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter INPUT_FORMAT_4 = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        carregarUsuario();
        int opcao;
        do {
            System.out.println("\n====== Blog de Notícias IBGE ======");
            System.out.println("Bem-vindo, " + usuario.getNome());
            System.out.println("1 - Buscar notícias");
            System.out.println("2 - Ver favoritos");
            System.out.println("3 - Ver para ler depois");
            System.out.println("4 - Ver notícias lidas");
            System.out.println("5 - Vizualisar listas");
            System.out.println("6 - Sair");

            opcao = lerInt("Escolha uma opção: ", 1, 6);

            switch (opcao) {
                case 1 -> buscarNoticias();
                case 2 -> {
                    exibirLista(usuario.getFavoritos(), "Favoritos");
                    menuEditarLista(usuario.getFavoritos(), "Favoritos");
                }
                case 3 -> {
                    exibirLista(usuario.getParaLerDepois(), "Para Ler Depois");
                    menuEditarLista(usuario.getParaLerDepois(), "Para Ler Depois");
                }
                case 4 -> {
                    exibirLista(usuario.getLidas(), "Lidas");
                    menuEditarLista(usuario.getLidas(), "Lidas");
                }
                case 5 -> visualizarListasMenu();
                case 6 -> {
                    JsonManager.salvar(usuario);
                    System.out.println("Saindo...");
                }
                default -> throw new IllegalArgumentException("Opção inválida: " + opcao);
            }

        } while (opcao != 6);
    }

    private static void visualizarListasMenu() {
      int op;
      do {
        System.out.println("\n=== Visualizar Listas ===");
        System.out.println("1 - Lidas");
        System.out.println("2 - Favoritos");
        System.out.println("3 - Para Ler Depois");
        System.out.println("4 - Voltar");
        op = lerInt("Escolha uma opção: ", 1, 4);
        switch (op) {
          case 1 -> {
              exibirLista(usuario.getLidas(), "Lidas");
              menuEditarLista(usuario.getLidas(), "Lidas");
              }
          case 2 -> {
              exibirLista(usuario.getFavoritos(), "Favoritos");
              menuEditarLista(usuario.getFavoritos(), "Favoritos");
              }
          case 3 -> {
              exibirLista(usuario.getParaLerDepois(), "Para Ler Depois");
              menuEditarLista(usuario.getParaLerDepois(), "Para Ler Depois");
              }
          case 4 -> {
              }
        }
      } while (op != 4);
    }

    private static void carregarUsuario() {
        usuario = JsonManager.carregar();
        if (usuario == null) {
            String nome = lerTexto("Digite seu nome ou apelido: ");
            usuario = new Usuario(nome);
        }
    }

    private static void buscarNoticias() {
        List<Noticia> noticias = service.buscarNoticias();
        if (noticias.isEmpty()) {
            System.out.println("Nenhuma notícia encontrada.");
            return;
        }

        int opcao;
        do {
            System.out.println("\n===== Buscar Notícias =====");
            System.out.println("1 - Listar todas");
            System.out.println("2 - Procurar por título ou palavra-chave");
            System.out.println("3 - Procurar por data (ex: 20251906, 19062025, 19/06/2025)");
            System.out.println("4 - Voltar");

            opcao = lerInt("Escolha uma opção: ", 1, 4);

            switch (opcao) {
                case 1 -> mostrarNoticias(noticias);
                case 2 -> {
                    String chave = lerTexto("Digite a palavra-chave: ").toLowerCase();
                    List<Noticia> filtradas = noticias.stream()
                            .filter(n -> n.getTitulo().toLowerCase().contains(chave) ||
                                    n.getIntroducao().toLowerCase().contains(chave))
                            .collect(Collectors.toList());
                    mostrarNoticias(filtradas);
                }
                case 3 -> {
                    LocalDate data = lerDataFlexivel("Digite a data: ");
                    if (data == null) {
                        System.out.println("Data inválida. Voltando ao menu.");
                    } else {
                        String dataFormatadaApi = data.format(DateTimeFormatter.ISO_LOCAL_DATE);
                        List<Noticia> porData = noticias.stream()
                                .filter(n -> n.getDataPublicacao() != null && n.getDataPublicacao().toString().equals(dataFormatadaApi))
                                .collect(Collectors.toList());
                        mostrarNoticias(porData);
                    }
                }
                case 4 -> {
                }
            }
        } while (opcao != 4);
    }

    private static void mostrarNoticias(List<Noticia> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhuma notícia encontrada.");
            return;
        }

        for (int i = 0; i < lista.size(); i++) {
            Noticia n = lista.get(i);
            String lidaMark = n.isLida() ? "LIDA " : "";
            System.out.println("[" + (i + 1) + "] " + lidaMark + n.getTitulo());
        }

        int escolha = lerInt("Digite o número da notícia para ver mais detalhes ou 0 para voltar: ", 0, lista.size());

        if (escolha == 0){
          return;
        }

        Noticia selecionada = lista.get(escolha - 1);
        System.out.println(selecionada);

        System.out.println("1 - Marcar como lida");
        System.out.println("2 - Adicionar aos favoritos");
        System.out.println("3 - Adicionar para ler depois");
        System.out.println("4 - Voltar");

        int acao = lerInt("Escolha uma ação: ", 1, 4);

        switch (acao) {
            case 1 -> {
                selecionada.marcarComoLida();
                usuario.adicionarLida(selecionada);
                System.out.println("Marcada como lida.");
            }
            case 2 -> {
                usuario.adicionarFavorito(selecionada);
                System.out.println("Adicionada aos favoritos.");
            }
            case 3 -> {
                usuario.adicionarParaLerDepois(selecionada);
                System.out.println("Adicionada na lista de 'Ler Depois'.");
            }
            case 4 -> {
            }
        }
    }

    private static void exibirLista(List<Noticia> lista, String nomeLista) {
        if (lista == null) {
            System.out.println("Lista " + nomeLista + " não encontrada.");
            return;
        }
        if (lista.isEmpty()) {
            System.out.println(nomeLista + " está vazia.");
            return;
        }

        System.out.println("\n==== " + nomeLista + " ====");
        System.out.println("1 - Ordenar por título");
        System.out.println("2 - Ordenar por data");
        System.out.println("3 - Ordenar por tipo");
        System.out.println("4 - Mostrar sem ordenação");

        int op = lerInt("Escolha uma opção: ", 1, 4);
        Comparator<Noticia> comparator = null;

        switch (op) {
            case 1 -> comparator = Comparator.comparing(Noticia::getTitulo, String.CASE_INSENSITIVE_ORDER);
            case 2 -> comparator = Comparator.comparing(Noticia::getDataPublicacao, Comparator.nullsLast(Comparator.naturalOrder()));
            case 3 -> comparator = Comparator.comparing(Noticia::getTipo, String.CASE_INSENSITIVE_ORDER);
            case 4 -> {
            }
        }

        List<Noticia> ordenada = (comparator != null) ? lista.stream().sorted(comparator).toList() : lista;

        for (Noticia n : ordenada) {
            System.out.println(n);
        }
    }

    private static void menuEditarLista(List<Noticia> lista, String nomeLista) {
        if (lista == null) {
          System.out.println("Lista " + nomeLista + " não encontrada.");
          return;
      }
      if (lista.isEmpty()) {
          System.out.println(nomeLista + " está vazia.");
          return;
      }
        System.out.println("\nDeseja remover alguma notícia da lista " + nomeLista + "?");
        System.out.println("1 - Sim");
        System.out.println("2 - Não");

        int opcao = lerInt("Escolha: ", 1, 2);
        if (opcao == 1) {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + lista.get(i).getTitulo());
            }
            int rem = lerInt("Digite o número da notícia para remover (0 para cancelar): ", 0, lista.size());
            if (rem != 0) {
                Noticia removida = lista.remove(rem - 1);
                System.out.println("Notícia removida da lista " + nomeLista + ": " + removida.getTitulo());
            }
        }
    }

    private static int lerInt(String mensagem, int min, int max) {
        while (true) {
            try {
                System.out.print(mensagem);
                String entrada = sc.nextLine().trim();

                if (!entrada.matches("\\d+")) {
                    System.out.println("Digite apenas números válidos.");
                    continue;
                }

                long valorLong = Long.parseLong(entrada);

                if (valorLong > Integer.MAX_VALUE) {
                    System.out.println("Número muito grande.");
                    continue;
                }

                int valor = (int) valorLong;

                if (valor < min || valor > max) {
                    System.out.printf("Digite um número entre %d e %d.%n", min, max);
                    continue;
                }

                return valor;

            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        String regex = "^[a-zA-ZÀ-ú0-9çÇ\\s]+$"; // Aceita letras, números e espaços

        while (true) {
            System.out.print(mensagem);
            String texto = sc.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("Entrada não pode ser vazia.");
                continue;
            }

            if (!Pattern.matches(regex, texto)) {
                System.out.println("Entrada contém caracteres inválidos. Permitido: apenas letras, números e espaços.");
                continue;
            }

            return texto;
        }
    }

    private static LocalDate lerDataFlexivel(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) return null;

            LocalDate data = parseDateFlexible(input);
            if (data != null) return data;

            System.out.println("Data inválida. Use formatos como: 20251906, 19062025, 19/06/2025, 2025-06-19.");
        }
    }

    private static LocalDate parseDateFlexible(String input) {
        DateTimeFormatter[] formatters = {
                INPUT_FORMAT_1,
                INPUT_FORMAT_2,
                INPUT_FORMAT_3,
                INPUT_FORMAT_4
        };

        for (DateTimeFormatter fmt : formatters) {
            try {
                return LocalDate.parse(input, fmt);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    public static DateTimeFormatter getOUTPUT_FORMAT() {
        return OUTPUT_FORMAT;
    }
}
