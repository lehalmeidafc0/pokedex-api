package br.com.fiap.dao;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Roda uma vez quando a API sobe: se a tabela DDD_POKEMONS ainda não existir
 * no Oracle, ela é criada (com 3 Pokémons de exemplo).
 * Assim não é obrigatório rodar o script sql/criar_tabela.sql no SQL Developer.
 */
@Component
public class CriadorDeTabela implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        try {
            Connection conexao = ConnectionFactory.getConnection();
            if (conexao == null) {
                System.out.println("Sem conexão com o banco: tabela não verificada.");
                return;
            }

            String sqlExiste = "select count(*) from user_tables where table_name = 'DDD_POKEMONS'";
            try (PreparedStatement ps = conexao.prepareStatement(sqlExiste);
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    System.out.println("Tabela DDD_POKEMONS já existe.");
                    return;
                }
            }

            try (Statement st = conexao.createStatement()) {
                st.execute("CREATE TABLE DDD_POKEMONS ("
                        + " CODIGO NUMBER GENERATED ALWAYS AS IDENTITY,"
                        + " NOME VARCHAR2(100),"
                        + " TIPO VARCHAR2(50),"
                        + " ALTURA NUMBER(5,2),"
                        + " PESO NUMBER(6,2),"
                        + " DATA_DA_CAPTURA DATE,"
                        + " PRIMARY KEY (CODIGO))");
                st.executeUpdate("INSERT INTO DDD_POKEMONS (NOME, TIPO, ALTURA, PESO, DATA_DA_CAPTURA)"
                        + " VALUES ('Pikachu', 'Elétrico', 0.4, 6.0, DATE '2025-03-10')");
                st.executeUpdate("INSERT INTO DDD_POKEMONS (NOME, TIPO, ALTURA, PESO, DATA_DA_CAPTURA)"
                        + " VALUES ('Bulbasaur', 'Planta', 0.7, 6.9, DATE '2025-04-22')");
                st.executeUpdate("INSERT INTO DDD_POKEMONS (NOME, TIPO, ALTURA, PESO, DATA_DA_CAPTURA)"
                        + " VALUES ('Charmander', 'Fogo', 0.6, 8.5, DATE '2025-06-05')");
            }
            System.out.println("Tabela DDD_POKEMONS criada com sucesso!");

        } catch (Exception e) {
            // Não derruba a API: só avisa no console
            System.out.println("Não foi possível verificar/criar a tabela: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }
}
