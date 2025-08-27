package servlet.pages.task;

import custom.annotations.Rota;
import h2factory.BeanFactory;
import servlet.pages.Page;
import task.TaskDao;

import java.util.Map;

@Rota("/deletar-task")
public class DeletarTaskPage implements Page {
    TaskDao taskDaoJdbc = BeanFactory.TaskDao();

    public String render(Map<String, Object> parameters) {
        if (parameters.containsKey("id")) {
            int id = Integer.parseInt(parameters.get("id").toString());
            taskDaoJdbc.delete(id);
        }

        return "<meta http-equiv='refresh' content='0; url=/listar-task' />";
    }
}
