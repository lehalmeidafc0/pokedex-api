package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

// Classe de conexão com o Oracle (padrões Factory e Singleton, como na aula).
// O usuário e a senha não ficam no código: vêm das variáveis de ambiente DB_URL, DB_USER e DB_PASSWORD.
//
// Mudança em relação ao slide: o Oracle da FIAP limita quantas conexões cada aluno pode
// ter abertas ao mesmo tempo (erro ORA-02391), e quando chegavam duas requisições juntas
// uma fechava a conexão da outra. Então agora só uma requisição usa o banco por vez.
// Se chegar outra, ela espera numa fila (o Semaphore) até a primeira terminar.
public class ConnectionFactory {

    private static Connection connection;

    // fila: só 1 requisição usa o banco por vez (true = respeita a ordem de chegada)
    private static final Semaphore vez = new Semaphore(1, true);

    // guarda se a requisição atual já está com a vez
    private static final ThreadLocal<Boolean> estaComAVez = ThreadLocal.withInitial(() -> false);

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            connection = null;
            liberarAVez();
        }
    }

    public static Connection getConnection() {
        pegarAVez();
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Busca dados de conexão nas variáveis de ambiente
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Variáveis de ambiente do banco não configuradas!");
            }

            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Erro nome da classe: " + e.getMessage());
        } catch (RuntimeException e) {
            liberarAVez();
            throw e;
        }
        if (connection == null) {
            // não conseguiu conectar, então libera a vez para a fila não travar
            liberarAVez();
        }
        return connection;
    }

    private static void pegarAVez() {
        if (!estaComAVez.get()) {
            vez.acquireUninterruptibly();
            estaComAVez.set(true);
        }
    }

    private static void liberarAVez() {
        if (estaComAVez.get()) {
            estaComAVez.set(false);
            vez.release();
        }
    }
}
