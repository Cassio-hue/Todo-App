package custommvc.servlet.pages;

import h2factory.task.TaskDao;

import java.util.List;
import java.util.Map;

public interface Page {
    TaskDao jdbc = null;
    
    String render(Map<String, Object> parameters);
}