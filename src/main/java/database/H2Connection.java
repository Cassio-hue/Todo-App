package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Connection {
    public static void main(String[] args) throws SQLException {
        String jdbcURL = "jdbc:h2:mem:test";
//        String jdbcURL = "jdbc:h2:~/test";
//        String username = "sa";
//        String password = "1234";

        Connection connection = DriverManager.getConnection(jdbcURL);
        System.out.println("Conexão Aberta!");
        connection.close();
        System.out.println("Conexão Fechada!");

    }
}
