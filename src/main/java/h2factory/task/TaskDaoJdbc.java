package h2factory.task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskDaoJdbc implements TaskDao {
    private final Connection connection;

    public TaskDaoJdbc() {
        this.connection = getConnection();
        criarTabela();
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:h2:mem:db1", "sa", "");
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao iniciar conexão", ex);
        }
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
            Logger.getLogger(TaskDaoJdbc.class.getName()).log(Level.SEVERE, "Erro ao criar tabela", ex);
        }
    }

    @Override
    public boolean insert(Task t) {
        String sql = """
                    INSERT INTO Task (
                        descricao,
                        concluido
                    ) VALUES (?, ?);
                """;
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setString(1, t.getDescricao());
            ps.setBoolean(2, t.getConcluido());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDaoJdbc.class.getName()).log(Level.SEVERE, "Erro ao adicionar tarefa", ex);
            return false;
        }
    }

    @Override
    public List<Task> list() {
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
        } catch (SQLException ex) {
            Logger.getLogger(TaskDaoJdbc.class.getName()).log(Level.SEVERE, "Erro ao listar tarefas", ex);
        }

        return tasks;
    }

    @Override
    public Task getById(int id) {
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
        } catch (SQLException ex) {
            Logger.getLogger(TaskDaoJdbc.class.getName()).log(Level.SEVERE, "Erro ao lista tarefa de ID: " + id, ex);
        }
        return null;
    }

    @Override
    public boolean update(Task t) {
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
        } catch (SQLException ex) {
            Logger.getLogger(TaskDaoJdbc.class.getName()).log(Level.SEVERE, "Erro ao atualizar tarefa de ID: " + t.getId(), ex);
            return false;
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Task WHERE id = ?;";
        try (PreparedStatement ps = this.connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaskDaoJdbc.class.getName()).log(Level.SEVERE, "Erro ao deletar tarefa de ID: " + id, ex);
        }
    }
}
