package h2factory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import task.Task;
import task.TaskDao;
import task.TaskDaoHibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.hibernate.cfg.JdbcSettings.*;

public class BeanFactory {
    private static final String URL_MEM = "jdbc:h2:mem:db1";
    private static final String URL_FILE = "jdbc:h2:./data/tododb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static TaskDao TaskDao() {
        return new TaskDaoHibernate();
    }

    public static SessionFactory getSessionFactory() {
        var sessionFactory = new Configuration()
                .addAnnotatedClass(Task.class)
                .setProperty(JAKARTA_JDBC_URL, URL_MEM)
                .setProperty(JAKARTA_JDBC_USER, USER)
                .setProperty(JAKARTA_JDBC_PASSWORD, PASSWORD)
                .setProperty("hibernate.agroal.maxSize", 20)
                .buildSessionFactory();

        sessionFactory.getSchemaManager().exportMappedObjects(true);

        return sessionFactory;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL_MEM, USER, PASSWORD);
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao iniciar conexão", ex);
        }
    }
}
