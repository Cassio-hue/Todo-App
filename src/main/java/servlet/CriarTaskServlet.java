package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.task.CriarTaskPage;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;

public class CriarTaskServlet extends HttpServlet {
    TaskDaoJdbc taskDaoJdbc;


    public CriarTaskServlet() {
        this.taskDaoJdbc = new TaskDaoJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println(new CriarTaskPage().render());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String desc = request.getParameter("descricao");
        Boolean status = Boolean.parseBoolean(request.getParameter("concluido"));
        if (desc != null && !desc.isBlank()) {
            Task novaTask = new Task();
            novaTask.setDescricao(desc);
            novaTask.setConcluido(status);
            taskDaoJdbc.save(novaTask);
        }
        response.sendRedirect(request.getContextPath() + "/listar-task");
    }
}
