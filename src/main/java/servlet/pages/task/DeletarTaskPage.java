package servlet.pages.task;

import custom.annotations.Rota;
import servlet.pages.Page;
import task.TaskDaoJdbc;

import java.util.Map;

@Rota("/deletar-task")
public class DeletarTaskPage implements Page {
    TaskDaoJdbc taskDaoJdbc = new TaskDaoJdbc();

    public String render(Map<String, Object> parameters) {
        if (parameters.containsKey("id")) {
            int id = Integer.parseInt(parameters.get("id").toString());
            taskDaoJdbc.delete(id);
        }

        return "<meta http-equiv='refresh' content='0; url=/listar-task' />";
    }
}
