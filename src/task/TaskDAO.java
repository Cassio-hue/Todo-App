package task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDAO {

    default void criarTabela() throws SQLException {
    }

    default void add(Task t) throws SQLException {
    }

    default List<Task> list() throws SQLException {
        return List.of();
    }

    default Task list(int id) throws SQLException {
        return null;
    }

    default boolean update(Task t) throws SQLException {
        return false;
    }

    default void delete(int id) throws SQLException {
    }
}
