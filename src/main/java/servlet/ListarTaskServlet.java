package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.task.ListarTaskPage;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;
import java.util.List;

public class ListarTaskServlet extends HttpServlet {
    TaskDaoJdbc taskDaoJdbc;

    public ListarTaskServlet() {
        this.taskDaoJdbc = new TaskDaoJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<Task> tasks = taskDaoJdbc.list();
        response.getWriter().println(new ListarTaskPage().render(tasks));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/listar-task");
    }
}
