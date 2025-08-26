package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.task.EditarTaskPage;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;

public class EditarTaskServlet extends HttpServlet {
    TaskDaoJdbc taskDaoJdbc;


    public EditarTaskServlet() {
        this.taskDaoJdbc = new TaskDaoJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        if  (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("/listar-task");
            return;
        }

        Task task = taskDaoJdbc.getById(Integer.parseInt(idStr));

        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println(new EditarTaskPage());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
        response.sendRedirect(request.getContextPath() + "/listar-task");
    }
}
