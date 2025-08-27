package h2factory;

import task.TaskDao;
import task.TaskDaoHibernate;

public class BeanFactory {
    public static TaskDao TaskDao() {
        return new TaskDaoHibernate();
    }
}
