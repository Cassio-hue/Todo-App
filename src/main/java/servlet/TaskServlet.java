package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pages.task.CriarTaskPage;
import pages.task.DeletarTaskPage;
import pages.task.EditarTaskPage;
import pages.task.ListarTaskPage;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TaskServlet extends HttpServlet {
    TaskDaoJdbc taskDaoJdbc;


    public TaskServlet() throws SQLException {
        this.taskDaoJdbc = new TaskDaoJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getRequestURI();
        if (pathInfo == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        response.setContentType("text/html; charset=UTF-8");

        switch (pathInfo) {
            case "/listar-task":
                List<Task> tasks = taskDaoJdbc.list();
                response.getWriter().println(new ListarTaskPage().render(tasks));
                break;

            case "/criar-task":
                response.getWriter().println(new CriarTaskPage().render());
                break;

            case "/editar-task":
                response.getWriter().println(new EditarTaskPage().render());
                break;

            case "/deletar-task":
                response.getWriter().println(new DeletarTaskPage().render());
                break;

            default:
                response.sendError(404);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getRequestURI();
        if (action == null) {
            response.sendError(400);
            return;
        }

        switch (action) {
            case "/criar-task":
                String desc = request.getParameter("descricao");
                Boolean status = Boolean.parseBoolean(request.getParameter("concluido"));
                if (desc != null && !desc.isBlank()) {
                    Task novaTask = new Task();
                    novaTask.setDescricao(desc);
                    novaTask.setConcluido(status);
                    taskDaoJdbc.save(novaTask);
                }
                break;

            case "/editar-task":
                String idStr = request.getParameter("id");
                String descricao = request.getParameter("descricao");
                String concluidoParam = request.getParameter("concluido");

                if (idStr != null) {
                    try {
                        Task task = taskDaoJdbc.getById(Integer.parseInt(idStr));
                        if (task != null) {
                            task.setConcluido(Boolean.parseBoolean(concluidoParam));
                            if (descricao != null && !descricao.isBlank()) {
                                task.setDescricao(descricao);
                            }
                            taskDaoJdbc.update(task);
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "/deletar-task":
                String id = request.getParameter("id");
                if (id != null) {
                    taskDaoJdbc.delete(Integer.parseInt(id));
                }
                break;

            default:
                response.sendError(400);
        }

        response.sendRedirect(request.getContextPath() + "/listar-task");
    }
}
