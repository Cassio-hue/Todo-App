package task;

import h2factory.task.Task;
import h2factory.task.TaskDaoJdbc;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskDaoJdbcTest {
    private static Connection connection;
    private static TaskDaoJdbc dao;
    private static Task task;
    private static Task persistedTask;

    @BeforeAll
    public static void setUp() throws SQLException {
        dao = new TaskDaoJdbc();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Order(0)
    @Test
    @DisplayName("TaskDAO Criar tarefa")
    void criarTarefa() {
        task = new Task("Tomar agua");
        assertTrue(dao.insert(task));
    }

    @Order(1)
    @Test
    @DisplayName("TaskDAO Listar uma tarefa")
    void listarTarefa() {
        List<Task> tasks = dao.list();
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        persistedTask = tasks.stream().filter(t -> t.getDescricao().equals(task.getDescricao())).findFirst().orElse(null);
        assertNotNull(persistedTask);
    }

    @Order(2)
    @Test
    @DisplayName("TaskDAO Editar tarefa")
    void editarTarefas() {
        List<Task> tasks = dao.list();
        assertFalse(tasks.isEmpty());
        assertNotNull(persistedTask);

        persistedTask.setDescricao("Fazer o AppTodo");
        persistedTask.setConcluido(true);

        boolean isUpdated = dao.update(persistedTask);
        assertTrue(isUpdated);

        Task updatedTaskDB = dao.getById(persistedTask.getId());

        assertEquals(persistedTask.getId(), updatedTaskDB.getId());
        assertEquals(persistedTask.getDescricao(), updatedTaskDB.getDescricao());
        assertEquals(persistedTask.getConcluido(), updatedTaskDB.getConcluido());

        persistedTask = updatedTaskDB;
    }

    @Order(3)
    @Test
    @DisplayName("TaskDAO Deletar tarefa")
    void  deletarTarefa() {
        assertTrue(dao.list().contains(persistedTask));
        dao.delete(persistedTask.getId());
        assertFalse(dao.list().contains(persistedTask));
    }
}