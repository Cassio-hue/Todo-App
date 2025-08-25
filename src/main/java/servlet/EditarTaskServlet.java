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

public class EditarTaskServlet extends HttpServlet {
    TaskDaoJdbc taskDaoJdbc;


    public EditarTaskServlet() throws SQLException {
        this.taskDaoJdbc = new TaskDaoJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println(new EditarTaskPage().render());
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
        response.sendRedirect(request.getContextPath() + "/");
    }
}
