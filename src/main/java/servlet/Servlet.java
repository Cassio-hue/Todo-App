package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.ListarTaskPage;
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

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {

        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        String concluidoParam = req.getParameter("concluido");

        if (action == null) {
            List<Task> tasks;
            try {
                tasks = dao.list();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(new ListarTaskPage().render(tasks));
            return;
        }

        switch (action) {
            case "create":
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
                break;
            case "toggle":
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
                break;
            case "delete":
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
                break;
            default:
                break;
        }

        resp.sendRedirect(req.getContextPath() + "/listar");
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
