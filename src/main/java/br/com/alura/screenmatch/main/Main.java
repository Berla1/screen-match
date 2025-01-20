package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=94d42d0b";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();
    private SerieRepository repositorio;
    private Optional<Serie> serieBusca;


    public Main(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Buscar top 5 séries
                    7 - Buscar séries por categoria
                    8 - Buscar séries por máximo de temporadas
                    9 - Buscar episódio por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episodios a partir de um ano
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTopSeries();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarSeriePorTotalTemporadas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSeriePorTitulo() {
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.next();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da série: " + serieBusca.get());
        } else {
            System.out.println("Série nao encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.print("Qual o nome para busca: ");
        String nomeAtor = leitura.next();
        System.out.print("Avaliações a partir de qual valor: ");
        Double avaliacaoAtor = leitura.nextDouble();

        List<Serie> seriesBuscadas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacaoAtor);
        System.out.println("\nAs melhores séries que " + nomeAtor + " trabalhou:");
        seriesBuscadas.forEach(s -> System.out.println("Título: " + s.getTitulo() + " avaliação:" + s.getAvaliacao()));

    }

    private void buscarTopSeries() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByAvaliacaoDesc();
        topSeries.forEach(s -> System.out.println("Título: " + s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.print("Digite a categoria desejada: ");
        var categoriaUser = leitura.next();
        Categoria categoria = Categoria.fromPortugues(categoriaUser);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);

        System.out.println("Series listadas pela categoria " + categoriaUser);
        seriesPorCategoria.forEach(s -> System.out.println("Título: " + s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorTotalTemporadas() {
        System.out.print("Digite o máximo de temporadas: ");
        var maxTemporadas = leitura.nextInt();
        System.out.print("Digite a avaliação mínima: ");
        var minAvaliacao = leitura.nextDouble();

        List<Serie> seriesMaxTemporada = repositorio.seriesPorTemporadaEAvaliacao(maxTemporadas, minAvaliacao);
        seriesMaxTemporada.forEach(s -> System.out.println("Título: " + s.getTitulo() + " avaliação: " + s.getAvaliacao() + "\n"));


    }

    private void buscarEpisodioPorTrecho() {
        System.out.print("Qual o nome do episodio para busca: ");
        String trechoEpisodio = leitura.next();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie  = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);

            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao() ));
        }
    }

    private void buscarEpisodiosPorData(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();

            System.out.print("Digite o ano limite de lançamento: ");
            var anoLancamento = leitura.nextInt();

            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);

        }
    }
}
