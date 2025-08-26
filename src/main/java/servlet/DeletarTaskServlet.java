package servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import task.TaskDaoJdbc;

import java.io.IOException;

public class DeletarTaskServlet extends HttpServlet {
    TaskDaoJdbc taskDaoJdbc;


    public DeletarTaskServlet() {
        this.taskDaoJdbc = new TaskDaoJdbc();
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
