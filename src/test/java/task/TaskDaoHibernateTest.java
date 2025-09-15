package task;

import h2factory.task.Task;
import h2factory.task.TaskDao;
import h2factory.task.TaskDaoHibernate;
import org.junit.jupiter.api.*;
import springmvc.AppConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TaskDaoHibernateTest {
    private TaskDao taskDaoHibernate;

    @BeforeEach
    void setUp() {
        taskDaoHibernate = new TaskDaoHibernate(AppConfig.sessionFactory());
    }

    @Test
    @DisplayName("Adicionar uma tarefa")
    void insert() {
        Task task = new Task("Tarefa 1");
        Assertions.assertTrue(taskDaoHibernate.insert(task));
    }

    @Test
    @DisplayName("Listar tarefas")
    void list() {
        Task task = new Task("Tarefa 1");
        taskDaoHibernate.insert(task);
        List<Task> tasks = taskDaoHibernate.list();

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        Task persistedTask = tasks.stream()
                .filter(t -> t.getDescricao().equals(task.getDescricao()))
                .findFirst()
                .orElse(null);
        assertNotNull(persistedTask);
    }

    @Test
    @DisplayName("Atualizar tarefa")
    void update() {
        Task task = new Task("Tarefa 1");
        List<Task> tasks = taskDaoHibernate.list();
        Task persistedTask = tasks.stream()
                .filter(t -> t.getDescricao().equals(task.getDescricao()))
                .findFirst()
                .orElse(null);
        assertNotNull(persistedTask);
        assertFalse(tasks.isEmpty());

        persistedTask.setDescricao("Fazer o AppTodo");
        persistedTask.setConcluido(true);
        assertTrue(taskDaoHibernate.update(persistedTask));

        Task updatedTaskDB = taskDaoHibernate.getById(persistedTask.getId());
        assertEquals(persistedTask.getId(), updatedTaskDB.getId());
        assertEquals(persistedTask.getDescricao(), updatedTaskDB.getDescricao());
        assertEquals(persistedTask.getConcluido(), updatedTaskDB.getConcluido());
    }

    @Test
    @DisplayName("Deletar tarefa")
    void delete() {
        Task task = new Task("Tarefa 1");
        taskDaoHibernate.insert(task);

        List<Task> tasks = taskDaoHibernate.list();
        Task persistedTask = tasks.stream()
                .filter(t -> t.getDescricao().equals(task.getDescricao()))
                .findFirst()
                .orElse(null);
        assertNotNull(persistedTask);

        assertTrue(taskDaoHibernate.list().contains(persistedTask));

        taskDaoHibernate.delete(persistedTask.getId());
        List<Task> tasksAfterDelete = taskDaoHibernate.list();
        assertFalse(tasksAfterDelete.contains(persistedTask));
    }
}