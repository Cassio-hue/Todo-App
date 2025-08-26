package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.HomePage;
import servlet.pages.NotFoundPage;
import servlet.pages.task.CriarTaskPage;
import servlet.pages.task.DeletarTaskPage;
import servlet.pages.task.EditarTaskPage;
import servlet.pages.task.ListarTaskPage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MiniServletMVC extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        Map<String, Object> parameters = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            parameters.put(key, value[0]);
        });

        response.setContentType("text/html; charset=UTF-8");
        switch (uri) {
            case "/":
                response.getWriter().write(HomePage.render());
                break;
            case "/listar-task":
                response.getWriter().println(new ListarTaskPage().render(parameters));
                break;
            case "/editar-task":
                response.getWriter().println(new EditarTaskPage().render(parameters));
                break;
            case "/deletar-task":
                response.getWriter().println(new DeletarTaskPage().render(parameters));
                break;
            case "/criar-task":
                response.getWriter().println(new CriarTaskPage().render(parameters));
                break;
            default:
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(NotFoundPage.render());
                break;
        }
    }
}
