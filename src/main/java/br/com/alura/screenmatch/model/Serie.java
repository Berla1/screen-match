package br.com.alura.screenmatch.model;
import jakarta.persistence.*;

import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private Integer totalTemporadas;
    private Double avaliacao;

    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;

    private String poster;
    private String sinopse;

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        // tenta obter um valor double, se der errado o valor vira 0
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        // .trim() tira todos os espaços em banco e quebras de linha
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();

    }

    public Serie() {} // construtor sem parametros para o jpa instanciar o objeto

    @Override
    public String toString() {
        return  "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';
    }

    public String getTitulo() {
        return titulo;
    }

    public Serie setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public String getSinopse() {
        return sinopse;
    }

    public Serie setSinopse(String sinopse) {
        this.sinopse = sinopse;
        return this;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public Serie setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
        return this;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Serie setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
        return this;
    }

    public String getAtores() {
        return atores;
    }

    public Serie setAtores(String atores) {
        this.atores = atores;
        return this;
    }

    public Categoria getGenero() {
        return genero;
    }

    public Serie setGenero(Categoria genero) {
        this.genero = genero;
        return this;
    }

    public String getPoster() {
        return poster;
    }

    public Serie setPoster(String poster) {
        this.poster = poster;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
