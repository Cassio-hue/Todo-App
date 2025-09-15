package h2factory.task;

import org.hibernate.SessionFactory;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public record TaskDaoHibernate(SessionFactory hibernateFactory) implements TaskDao {

    @Override
    public boolean insert(Task task) {
        try {
            hibernateFactory.inTransaction(s -> s.persist(task));
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao inserir tarefa", ex);
            return false;
        }
    }

    @Override
    public List<Task> list() {
        try {
            return hibernateFactory.fromTransaction(session -> session.createQuery("from Task", Task.class).list());
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao listar tarefas", ex);
            return null;
        }
    }

    @Override
    public Task getById(int id) {
        try {
            return hibernateFactory.fromTransaction(session -> session.find(Task.class, id));
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao listar tarefa de ID: " + id, ex);
            return null;
        }
    }

    @Override
    public boolean update(Task task) {
        try {
            hibernateFactory.inTransaction(session -> session.merge(task));
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TaskDaoHibernate.class.getName()).log(Level.SEVERE, "Erro ao atualizar tarefa", ex);
            return false;
        }
    }

    @Override
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