package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.task.DeletarTaskPage;
import task.TaskDaoJdbc;

import java.io.IOException;
import java.sql.SQLException;

public class DeletarTaskServlet extends HttpServlet {
    TaskDaoJdbc taskDaoJdbc;


    public DeletarTaskServlet() throws SQLException {
        this.taskDaoJdbc = new TaskDaoJdbc();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println(new DeletarTaskPage().render());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            taskDaoJdbc.delete(Integer.parseInt(id));
        }
        response.sendRedirect(request.getContextPath() + "/listar-task");
    }
}
