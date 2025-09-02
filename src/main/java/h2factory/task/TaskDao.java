package h2factory.task;

import java.util.List;

public interface TaskDao {
    boolean insert(Task t);

    List<Task> list();

    Task getById(int id);

    boolean update(Task t);

    void delete(int id);
}
