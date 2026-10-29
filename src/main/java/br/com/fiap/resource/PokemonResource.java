package br.com.fiap.resource;

import br.com.fiap.bo.PokemonBO;
import br.com.fiap.to.PokemonTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controller da API: responde as requisições em /pokedex e devolve JSON com o status certo.
@RestController
@RequestMapping("/pokedex")
public class PokemonResource {

    private PokemonBO pokemonBO = new PokemonBO();

    // GET /pokedex: 200 com a lista ou 404
    @GetMapping
    public ResponseEntity<List<PokemonTO>> findAll() {
        List<PokemonTO> pokemons = pokemonBO.findAll();
        if (pokemons != null) {
            return ResponseEntity.status(HttpStatus.OK).body(pokemons);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pokemons);
        }
    }

    // GET /pokedex/{codigo}: 200 com o Pokémon ou 404
    @GetMapping("/{codigo}")
    public ResponseEntity<?> findByCodigo(@PathVariable Long codigo) {
        PokemonTO pokemon = pokemonBO.findByCodigo(codigo);
        if (pokemon != null) {
            return ResponseEntity.status(HttpStatus.OK).body(pokemon);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokémon não encontrado!");
        }
    }

    // POST /pokedex: 201 ou 400
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid PokemonTO pokemon) {
        PokemonTO resultado = pokemonBO.save(pokemon);
        if (resultado != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar Pokémon");
        }
    }

    // DELETE /pokedex/{codigo}: 204 ou 404
    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> delete(@PathVariable Long codigo) {
        if (pokemonBO.delete(codigo)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Pokémon deletado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pokémon não encontrado!");
        }
    }

    // PUT /pokedex/{codigo}: 201 ou 400
    @PutMapping("/{codigo}")
    public ResponseEntity<?> update(@PathVariable Long codigo,
                                    @RequestBody @Valid PokemonTO pokemon) {
        pokemon.setCodigo(codigo);
        PokemonTO resultado = pokemonBO.update(pokemon);
        if (resultado != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar Pokémon (verifique se o código existe)");
        }
    }
}
