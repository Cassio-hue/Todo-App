package task;

import h2factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskDaoJdbc implements TaskDao {
    private final Connection connection;

    public TaskDaoJdbc() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
        criarTabela();
    }

    public TaskDaoJdbc(Connection connection) {
        this.connection = connection;
        criarTabela();

    }

    public void criarTabela() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS Task (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        descricao VARCHAR(255),
                        concluido BOOLEAN DEFAULT FALSE
                    );
                """;
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(TaskDaoJdbc.class.getName()).log(Level.SEVERE, "Erro ao criar Tabela Task", ex);
        }
    }
    public void deletarTabela() throws SQLException {
        String sql = "DROP TABLE IF EXISTS Task";
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public boolean save(Task t) throws SQLException {
        String sql = """
                    INSERT INTO Task (
                        descricao
                    ) VALUES (?);
                """;
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, t.getDescricao());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Task> list() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Task";

        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setDescricao(rs.getString("descricao"));
                t.setConcluido(rs.getBoolean("concluido"));
                tasks.add(t);
            }

            return tasks;
        }
    }

    public Task getById(int id) throws SQLException {
        String sql = "SELECT * FROM Task WHERE id = ?";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Task(
                            rs.getInt("id"),
                            rs.getString("descricao"),
                            rs.getBoolean("concluido")
                    );
                }
            }
        }
        return null;
    }

    public boolean update(Task t) throws SQLException {
        String sql = """
                    UPDATE Task SET
                        descricao = ?,
                        concluido = ?
                    WHERE id = ?;
                """;
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, t.getDescricao());
            ps.setBoolean(2, t.getConcluido());
            ps.setInt(3, t.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Task WHERE id = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
