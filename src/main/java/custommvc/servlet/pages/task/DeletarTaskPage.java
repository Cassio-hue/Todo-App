package custommvc.servlet.pages.task;

import custommvc.servlet.annotations.Rota;
import custommvc.servlet.pages.Page;
import h2factory.task.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Rota("/deletar-task")
public class DeletarTaskPage implements Page {
    @Qualifier("taskDaoJdbc")
    @Autowired
    TaskDao taskDaoJdbc;

    public String render(Map<String, Object> parameters) {
        if (parameters.containsKey("id")) {
            int id = Integer.parseInt(parameters.get("id").toString());
            taskDaoJdbc.delete(id);
        }

        return "<meta http-equiv='refresh' content='0; url=/custom-mvc/listar-task' />";
    }
}
