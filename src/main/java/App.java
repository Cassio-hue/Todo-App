import servlet.Servlet;
import task.TaskDaoJdbc;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {
    public static void main(String[] args) throws Exception {
        TaskDaoJdbc dao = new TaskDaoJdbc();
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new Servlet(dao)), "/app");

        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");

        server.join();
    }
}
