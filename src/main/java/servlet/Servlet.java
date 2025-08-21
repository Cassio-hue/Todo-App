package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Servlet extends HttpServlet {
    TaskDaoJdbc dao;


    public Servlet(TaskDaoJdbc dao) {
        this.dao = dao;
    }

    private void processRequest(
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, SQLException {

        String action = req.getParameter("action");
        if ("create".equals(action)) {
            String descricao = req.getParameter("descricao");
            if (descricao != null && !descricao.isBlank()) {
                Task novaTask = new Task();
                novaTask.setDescricao(descricao);
                novaTask.setConcluido(false);
                try {
                    dao.save(novaTask);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if ("toggle".equals(action)) {
            String idStr = req.getParameter("id");
            String concluidoParam = req.getParameter("concluido");

            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    boolean concluido = !Boolean.parseBoolean(concluidoParam);
                    Task task = dao.getById(id);
                    if (task != null) {
                        task.setConcluido(concluido);
                        dao.update(task);
                    }
                } catch (NumberFormatException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if ("delete".equals(action)) {
            String idStr = req.getParameter("id");

            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    Task task = dao.getById(id);
                    if (task != null) {
                        dao.delete(id);
                    }
                } catch (NumberFormatException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        List<Task> tasks = null;
        try {
            tasks = dao.list();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println(new ListarTaskPage().render(tasks));
    }

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
