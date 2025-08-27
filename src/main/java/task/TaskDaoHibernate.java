package task;

import h2factory.HibernateUtil;
import org.h2.engine.Database;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskDaoHibernate implements TaskDao {
    SessionFactory hibernateFactory;

    public TaskDaoHibernate() {
        hibernateFactory = HibernateUtil.getSessionFactory(HibernateUtil.DatabaseType.FILE);
    }

    public TaskDaoHibernate(HibernateUtil.DatabaseType dbType) {
        hibernateFactory = HibernateUtil.getSessionFactory(dbType);
    }

    public boolean insert(Task task) {
        Transaction transaction = null;

        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao inserir tarefa", ex);
            return false;
        }
    }

    public List<Task> list() {
        try (Session session = hibernateFactory.openSession()) {
            return session.createQuery("from Task", Task.class).list();
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao listar tarefas", ex);
        }

        return null;
    }

    public Task getById(int id) {
        try (Session session = hibernateFactory.openSession()) {
            return session.get(Task.class, id);
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao listar tarefa de ID: " + id, ex);
        }
        return null;
    }

    public boolean update(Task task) {
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(task);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao atualizar tarefa", ex);
            return false;
        }
    }

    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = hibernateFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(getById(id));
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao deletar tarefa", ex);
        }
    }
}