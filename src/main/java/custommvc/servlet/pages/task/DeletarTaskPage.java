package custommvc.servlet.pages.task;

import custommvc.servlet.annotations.Rota;
import custommvc.servlet.pages.Page;
import h2factory.task.TaskDao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Rota("/deletar-task")
public class DeletarTaskPage implements Page {

    private final TaskDao taskDao;
    DeletarTaskPage(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public String render(HttpServletRequest request, Map<String, Object> parameters) {
        if (parameters.containsKey("id")) {
            int id = Integer.parseInt(parameters.get("id").toString());
            taskDao.delete(id);
        }

        return "<meta http-equiv='refresh' content='0; url=/custom-mvc/listar-task' />";
    }
}
