package h2factory;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    public enum DatabaseType {
        FILE,
        IN_MEMORY
    }

    public static SessionFactory getSessionFactory(DatabaseType dbType) {
        switch (dbType) {
            case FILE -> sessionFactory = getSessionFactoryInFile();
            case IN_MEMORY -> sessionFactory = getSessionFactoryInMemory();
            default -> throw new IllegalArgumentException("Tipo de banco não suportado");
        }

        if (sessionFactory == null) {
            throw new RuntimeException("Erro ao criar conexão com o banco de dados");
        }

        return sessionFactory;
    }

    private static SessionFactory getSessionFactoryInFile() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception ex) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                Logger.getLogger(SessionFactory.class.getName()).log(Level.SEVERE, "Erro ao disponibilizar sessão do banco de dados", ex);
            }
        }
        return sessionFactory;
    }

    private static SessionFactory getSessionFactoryInMemory() {
        if (sessionFactory == null) {
            try {
                Map<String, Object> settings = new HashMap<>();
                settings.put("hibernate.connection.driver_class", "org.h2.Driver");
                settings.put("hibernate.connection.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
                settings.put("hibernate.connection.username", "sa");
                settings.put("hibernate.connection.password", "");
                settings.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
                settings.put("hibernate.hbm2ddl.auto", "create-drop");
                settings.put("hibernate.show_sql", "true");

                registry = new StandardServiceRegistryBuilder()
                        .applySettings(settings)
                        .build();

                MetadataSources sources = new MetadataSources(registry);

                // Entidades anotadas
                 sources.addAnnotatedClass(Task.class);

                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception ex) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, "Erro ao disponibilizar sessão de teste do banco em memória", ex);
            }
        }
        return sessionFactory;
    }
}
