package h2factory;

import h2factory.task.TaskDao;
import h2factory.task.TaskDaoHibernate;
import h2factory.task.TaskDaoJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskDAO {

    @Bean
    TaskDao taskDaoHibernate() {
        return new TaskDaoHibernate();
    }

    @Bean
    TaskDao taskDaoJdbc() {
        return new TaskDaoJdbc();
    }
}
