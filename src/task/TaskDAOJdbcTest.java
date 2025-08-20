package task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TaskDAOJdbcTest {
    private static Connection conn;
    private TaskDAOJdbc dao;

    @BeforeEach
    public void setUp() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }

        String jdbcURL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String password = "";
        conn = DriverManager.getConnection(jdbcURL, user, password);

        dao = new TaskDAOJdbc(conn);
        dao.deletarTabela();
        dao.criarTabela();
        dao.add(new Task("Tarefa 1"));
        dao.add(new Task("Tarefa 2"));
        dao.add(new Task("Tarefa 3"));
        dao.add(new Task("Tarefa 4"));
    }

    @Test
    @DisplayName("TaskDAO Criar tarefa")
    void criarTarefas() throws SQLException {
        dao.add(new Task("Fazer o AppTodo"));
        List<Task> tasks = dao.list();
        Task t = tasks.getLast();

        assertEquals("Fazer o AppTodo", t.getDescricao());
        assertFalse(t.getConcluido());
    }

    @Test
    @DisplayName("TaskDAO Editar tarefa")
    void editarTarefas() throws SQLException {
        List<Task> tasks = dao.list();
        Task t = tasks.getFirst();
        assertEquals(1, t.getId());
        assertEquals("Tarefa 1", t.getDescricao());
        assertFalse(t.getConcluido());

        Task updatedTask = new Task(t.getId(), t.getDescricao(), true);
        dao.update(updatedTask);
        Task updatedTaskDB = dao.list(t.getId());

        assertEquals(updatedTask.getId(), updatedTaskDB.getId());
        assertEquals(updatedTask.getDescricao(), updatedTaskDB.getDescricao());
        assertEquals(updatedTask.getConcluido(), updatedTaskDB.getConcluido());
    }

    @Test
    @DisplayName("TaskDAO Listar todas as tarefas")
    void listarTarefas() throws SQLException {
        List<Task> tasks = dao.list();

        assertEquals(4, tasks.size());
    }

    @Test
    @DisplayName("TaskDAO Listar uma tarefa")
    void listarTarefa() throws SQLException {
        List<Task> tasks = dao.list();
        Task task = tasks.getFirst();
        Task taskDB = dao.list(task.getId());

        assertEquals(task.getId(), taskDB.getId());
        assertEquals(task.getDescricao(), taskDB.getDescricao());
        assertEquals(task.getConcluido(), taskDB.getConcluido());
    }

    @Test
    @DisplayName("TaskDAO Deletar tarefa")
    void  deletarTarefa() throws SQLException {
        List<Task> tasks = dao.list();
        assertEquals(4, tasks.size());
        dao.delete(1);
        tasks = dao.list();
        assertEquals(3, tasks.size());
    }
}