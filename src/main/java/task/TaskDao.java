package task;

import java.util.List;

public interface TaskDao {
    default void criarTabela() {
    }

    default boolean insert(Task t) {
        return false;
    }

    default List<Task> list() {
        return List.of();
    }

    default Task getById(int id) {
        return null;
    }

    default boolean update(Task t) {
        return false;
    }

    default void delete(int id) {
    }
}
