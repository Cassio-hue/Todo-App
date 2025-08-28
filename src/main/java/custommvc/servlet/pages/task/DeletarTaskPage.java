package custommvc.servlet.pages.task;

import custommvc.servlet.annotations.Rota;
import h2factory.BeanFactory;
import custommvc.servlet.pages.Page;
import h2factory.task.TaskDao;

import java.util.Map;

@Rota("/deletar-task")
public class DeletarTaskPage implements Page {
    TaskDao taskDaoJdbc = BeanFactory.TaskDao();

    public String render(Map<String, Object> parameters) {
        if (parameters.containsKey("id")) {
            int id = Integer.parseInt(parameters.get("id").toString());
            taskDaoJdbc.delete(id);
        }

        return "<meta http-equiv='refresh' content='0; url=/custom-mvc/listar-task' />";
    }
}
