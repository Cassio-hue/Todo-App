package task;

import daoAPI.GenericDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TaskDao extends GenericDAO<Task> {
    private final static String TABLENAME = "TASK";

    public TaskDao(Connection con) {
        super(con, TABLENAME);
    }

    @Override
    protected Optional<Task> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM " + this.tableName;
        try (Statement stmt = this.con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));                     // Exemplo de campo inteiro
                task.setDescricao(rs.getString("descricao"));    // Exemplo de campo string
                task.setConcluida(rs.getBoolean("feito"));           // Exemplo de campo boolean
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // ou manipule como achar melhor
        }
        return tasks;
    }

    @Override
    protected void save(Task task) {

    }

    @Override
    protected void update(Task task, String[] params) {

    }

    @Override
    protected void delete(Task task) {

    }
}
