package custommvc.servlet;

import custommvc.servlet.annotations.Rota;
import custommvc.servlet.pages.NotFoundPage;
import custommvc.servlet.pages.Page;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MiniServletMVC extends HttpServlet { ;
    private final Map<String, Page> uriPageMap = new HashMap<>();
    private final Collection<Page> pages;

    @Autowired
    public MiniServletMVC(Collection<Page> pages) {
        this.pages = pages;
    }

    @PostConstruct
    public void init() {
        for (Page page : pages) {
            Rota rota = page.getClass().getAnnotation(Rota.class);
            if (rota != null) {
                uriPageMap.put(rota.value(), page);
            }
        }
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI().replaceFirst(request.getServletPath(), "");
        Map<String, Object> parameters = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            parameters.put(key, value[0]);
        });
        response.setContentType("text/html; charset=UTF-8");

        Page page = uriPageMap.get(path);

        if (page == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(NotFoundPage.render());
            return;
        }

        response.getWriter().write(page.render(parameters));
    }
}
