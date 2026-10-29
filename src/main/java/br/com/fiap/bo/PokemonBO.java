package br.com.fiap.bo;

import br.com.fiap.dao.PokemonDAO;
import br.com.fiap.to.PokemonTO;

import java.util.ArrayList;

/**
 * Business Object: camada das regras de negócio.
 * O Resource chama o BO, e o BO decide se pode ou não chamar o DAO.
 */
public class PokemonBO {

    private PokemonDAO pokemonDAO;

    public ArrayList<PokemonTO> findAll() {
        pokemonDAO = new PokemonDAO();
        // aqui se implementa a regra de negócios
        return pokemonDAO.findAll();
    }

    public PokemonTO findByCodigo(Long codigo) {
        pokemonDAO = new PokemonDAO();
        // aqui se implementa a regra de negócios
        return pokemonDAO.findByCodigo(codigo);
    }

    public PokemonTO save(PokemonTO pokemon) {
        pokemonDAO = new PokemonDAO();
        // aqui se implementa a regra de negócios
        // Na aula A40 validamos aqui que a data da captura deve estar no passado.
        // A partir da aula A41 essa regra passou a ser feita pelo Bean Validation
        // (anotação @Past em PokemonTO), por isso ficou comentada:
        // if (!pokemon.getDataDaCaptura().isBefore(LocalDate.now())) {
        //     return null;
        // }
        return pokemonDAO.save(pokemon);
    }

    public boolean delete(Long codigo) {
        pokemonDAO = new PokemonDAO();
        // aqui se implementa a regra de negócios
        return pokemonDAO.delete(codigo);
    }

    public PokemonTO update(PokemonTO pokemon) {
        pokemonDAO = new PokemonDAO();
        // aqui se implementa a regra de negócios
        return pokemonDAO.update(pokemon);
    }
}
