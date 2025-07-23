package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOManager {
    private Connection con;

    // Conectar pela interface: jdbc:h2:C:\Users\CassioBorges\Projetos\Todo-App\src\data\todo-app
    private final String jdbcURL = "jdbc:h2:./src/data/todo-app;AUTO_SERVER=TRUE";
    private final String username = "sa";
    private final String password = "1234";

    public DAOManager() {
    }

    public void open() throws SQLException {
        try {
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(jdbcURL, username, password);
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public void close() throws SQLException {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public Connection getConnection() {
        return con;
    }
}