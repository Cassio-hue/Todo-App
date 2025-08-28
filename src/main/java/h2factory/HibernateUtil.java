package h2factory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import task.Task;

import static org.hibernate.cfg.JdbcSettings.*;


public class HibernateUtil {
    private boolean testMode = false;

    public HibernateUtil() {}

    public HibernateUtil(boolean testMode) {
        this.testMode = testMode;
    }

    public SessionFactory getSessionFactory() {
        String url = testMode
                ? "jdbc:h2:mem:db1"
                : "jdbc:h2:./data/tododb";

        var sessionFactory = new Configuration()
                .addAnnotatedClass(Task.class)
                .setProperty(JAKARTA_JDBC_URL, url)
                .setProperty(JAKARTA_JDBC_USER, "sa")
                .setProperty(JAKARTA_JDBC_PASSWORD, "")
                .setProperty("hibernate.agroal.maxSize", 20)
//                .setProperty(SHOW_SQL, true)
//                .setProperty(FORMAT_SQL, true)
//                .setProperty(HIGHLIGHT_SQL, true)
                .buildSessionFactory();

        sessionFactory.getSchemaManager().exportMappedObjects(true);

        return sessionFactory;
    }
}
