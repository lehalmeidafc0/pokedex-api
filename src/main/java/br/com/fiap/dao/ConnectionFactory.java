package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões com o Oracle (padrões Factory e Singleton).
 * As credenciais NÃO ficam no código: são lidas das variáveis de ambiente
 * DB_URL, DB_USER e DB_PASSWORD (no IntelliJ e no servidor do Deploy).
 *
 * A conexão fica guardada em um ThreadLocal: cada requisição (thread) tem a sua.
 * Assim, se duas requisições chegarem ao mesmo tempo, uma não fecha a conexão da outra.
 */
public class ConnectionFactory {

    private static final ThreadLocal<Connection> connection = new ThreadLocal<>();

    public static void closeConnection() {
        try {
            Connection conexao = connection.get();
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            connection.remove();
        }
    }

    public static Connection getConnection() {
        try {
            Connection conexao = connection.get();
            if (conexao != null && !conexao.isClosed()) {
                return conexao;
            }
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Busca dados de conexão nas variáveis de ambiente
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Variáveis de ambiente do banco não configuradas!");
            }

            connection.set(DriverManager.getConnection(url, user, password));

        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Erro nome da classe: " + e.getMessage());
        }
        return connection.get();
    }
}
