package custommvc.servlet.pages;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface Page {
    String render(HttpServletRequest request, Map<String, Object> parameters);
}