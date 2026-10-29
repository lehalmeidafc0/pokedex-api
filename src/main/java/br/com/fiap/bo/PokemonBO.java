package br.com.fiap.bo;

import br.com.fiap.dao.PokemonDAO;
import br.com.fiap.to.PokemonTO;

import java.util.ArrayList;

// Camada das regras de negócio: o Resource chama o BO e o BO chama o DAO.
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
        // na A40 essa validação da data da captura ficava aqui.
        // na A41 passou para o Bean Validation (@Past no PokemonTO), então deixei comentada:
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
