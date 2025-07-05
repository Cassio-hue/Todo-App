package task;

import daoAPI.DaoAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TaskDao implements DaoAPI<Task> {
    private List<Task> tasks = new ArrayList<>();

    public TaskDao() {
        tasks.add(new Task(1, "Task 1"));
        tasks.add(new Task(2, "Task 2"));
    }

    @Override
    public Optional<Task> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> getAll() {
        return tasks;
    }

    @Override
    public void save(Task task) {
        this.tasks.add(task);
    }

    @Override
    public void update(Task task, String[] params) {
        if (tasks == null) throw new IllegalStateException("tasks list is null");
        task.setDescricao(Objects.requireNonNull(params[0], "Desc cannot be null"));
    }

    @Override
    public void delete(Task task) {
        tasks.remove(task);
    }
}
