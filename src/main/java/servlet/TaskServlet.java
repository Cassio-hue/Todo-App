package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.ListarTaskPage;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskServlet extends HttpServlet {
    TaskDaoJdbc dao;

    public TaskServlet(TaskDaoJdbc dao) {
        this.dao = dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Object> tasks;
        try {
            tasks = new ArrayList<>(dao.list());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html; charset=UTF-8");
        Map<String, List<Object>> pageMap = new HashMap<>();
        pageMap.put(ListarTaskPage.TASKS, tasks);
        response.getWriter().println(new ListarTaskPage().render(pageMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        String concluidoParam = request.getParameter("concluido");

        switch (action) {
            case "create":
                String descricao = request.getParameter("descricao");
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

        response.sendRedirect(request.getContextPath() + ListarTaskPage.ROUTE);
    }
}
