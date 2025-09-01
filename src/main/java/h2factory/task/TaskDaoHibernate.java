package h2factory.task;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hibernate.cfg.JdbcSettings.*;

public class TaskDaoHibernate implements TaskDao {
    SessionFactory hibernateFactory;

    public TaskDaoHibernate() {
        hibernateFactory = getSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        var sessionFactory = new Configuration()
                .addAnnotatedClass(Task.class)
                .setProperty(JAKARTA_JDBC_URL, "jdbc:h2:mem:db1")
                .setProperty(JAKARTA_JDBC_USER, "sa")
                .setProperty(JAKARTA_JDBC_PASSWORD, "")
                .buildSessionFactory();

        sessionFactory.getSchemaManager().exportMappedObjects(true);

        return sessionFactory;
    }

    public boolean insert(Task task) {
        try {
            hibernateFactory.inTransaction(s -> s.persist(task));
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao inserir tarefa", ex);
            return false;
        }
    }

    public List<Task> list() {
        try {
            return hibernateFactory.fromTransaction(session -> session.createQuery("from Task", Task.class).list());
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao listar tarefas", ex);
            return null;
        }
    }

    public Task getById(int id) {
        try {
            return hibernateFactory.fromTransaction(session -> session.find(Task.class, id));
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao listar tarefa de ID: " + id, ex);
            return null;
        }
    }

    public boolean update(Task task) {
        try {
            hibernateFactory.inTransaction(session -> session.merge(task));
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao atualizar tarefa", ex);
            return false;
        }
    }

    public void delete(int id) {
        try {
            hibernateFactory.inTransaction(session -> {
                Task task = session.find(Task.class, id);
                if (task != null) {
                    session.remove(task);
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao deletar tarefa", ex);
        }
    }
}