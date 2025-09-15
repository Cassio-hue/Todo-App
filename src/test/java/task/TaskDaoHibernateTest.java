package task;

import h2factory.task.Task;
import h2factory.task.TaskDao;
import h2factory.task.TaskDaoHibernate;
import org.junit.jupiter.api.*;
import springmvc.AppConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskDaoHibernateTest {
    private static TaskDao taskDaoHibernate;
    private Task task;
    private Task persistedTask;

    @BeforeAll
    public static void setUp() {
        taskDaoHibernate = new TaskDaoHibernate(AppConfig.sessionFactory());
    }

    @Order(0)
    @Test
    @DisplayName("Adicionar uma tarefa")
    void insert() {
        task = new Task("Tarefa 1");
        Assertions.assertTrue(taskDaoHibernate.insert(task));
    }

    @Order(1)
    @Test
    @DisplayName("Listar tarefas")
    void list() {
        List<Task> tasks = taskDaoHibernate.list();
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        persistedTask = tasks.stream().filter(t -> t.getDescricao().equals(task.getDescricao())).findFirst().orElse(null);
        assertNotNull(persistedTask);
    }

    @Order(2)
    @Test
    @DisplayName("Atualizar tarefa")
    void update() {
        List<Task> tasks = taskDaoHibernate.list();
        assertFalse(tasks.isEmpty());
        assertNotNull(persistedTask);

        persistedTask.setDescricao("Fazer o AppTodo");
        persistedTask.setConcluido(true);

        boolean isUpdated = taskDaoHibernate.update(persistedTask);
        assertTrue(isUpdated);

        Task updatedTaskDB = taskDaoHibernate.getById(persistedTask.getId());

        assertEquals(persistedTask.getId(), updatedTaskDB.getId());
        assertEquals(persistedTask.getDescricao(), updatedTaskDB.getDescricao());
        assertEquals(persistedTask.getConcluido(), updatedTaskDB.getConcluido());

        persistedTask = updatedTaskDB;
    }

    @Order(3)
    @Test
    @DisplayName("Deletar tarefa")
    void delete() {
        assertTrue(taskDaoHibernate.list().contains(persistedTask));
        taskDaoHibernate.delete(persistedTask.getId());
        assertFalse(taskDaoHibernate.list().contains(persistedTask));
    }
}