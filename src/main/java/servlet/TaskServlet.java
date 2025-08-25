package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pages.task.ListarTaskPage;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TaskServlet extends HttpServlet {
    TaskDaoJdbc dao;


    public TaskServlet() throws SQLException {
        this.dao = new TaskDaoJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks = dao.list();
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println(new ListarTaskPage().render(tasks));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            String desc = request.getParameter("descricao");
            Boolean status = Boolean.parseBoolean(request.getParameter("concluido"));
            if (desc != null && !desc.isBlank()) {
                Task novaTask = new Task();
                novaTask.setDescricao(desc);
                novaTask.setConcluido(status);
                dao.save(novaTask);
            }
        }

        else if ("toggle".equals(action)) {
            String idStr = request.getParameter("id");
            String concluidoParam = request.getParameter("concluido");

            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    boolean concluido = !Boolean.parseBoolean(concluidoParam);
                    Task task = dao.getById(id);
                    if (task != null) {
                        task.setConcluido(concluido);
                        dao.update(task);
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        else if ("delete".equals(action)) {
            String idStr = request.getParameter("id");

            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    Task task = dao.getById(id);
                    if (task != null) {
                        dao.delete(id);
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/listar-task");
    }
}
