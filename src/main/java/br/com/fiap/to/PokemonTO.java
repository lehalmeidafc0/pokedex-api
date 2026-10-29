package br.com.fiap.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// Representa um Pokémon da Pokedex. Cada atributo é uma coluna da tabela DDD_POKEMONS.
public class PokemonTO {

    private Long codigo;

    @NotBlank(message = "O nome do Pokémon é obrigatório")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O tipo do Pokémon é obrigatório")
    @Size(max = 50, message = "O tipo pode ter no máximo 50 caracteres")
    private String tipo;

    @NotNull(message = "A altura é obrigatória")
    @Positive(message = "A altura deve ser maior que zero")
    private Double altura;

    @NotNull(message = "O peso é obrigatório")
    @Positive(message = "O peso deve ser maior que zero")
    private Double peso;

    // não dá para capturar um Pokémon no futuro, então a data tem que ser no passado
    @NotNull(message = "A data da captura é obrigatória")
    @Past(message = "A data da captura deve ser uma data no passado")
    private LocalDate dataDaCaptura;

    public PokemonTO() {
    }

    public PokemonTO(Long codigo, String nome, String tipo, Double altura, Double peso, LocalDate dataDaCaptura) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.altura = altura;
        this.peso = peso;
        this.dataDaCaptura = dataDaCaptura;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public LocalDate getDataDaCaptura() {
        return dataDaCaptura;
    }

    public void setDataDaCaptura(LocalDate dataDaCaptura) {
        this.dataDaCaptura = dataDaCaptura;
    }
}
