package custommvc.servlet.pages;

import java.util.List;
import java.util.Map;

public interface Page {
    String render(Map<String, Object> parameters);
}