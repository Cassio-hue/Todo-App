import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.TaskServlet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder defaultServlet = new ServletHolder(DefaultServlet.class);
        defaultServlet.setInitParameter("resourceBase", "src/main/webapp");
        defaultServlet.setInitParameter("dirAllowed", "true");
        context.addServlet(defaultServlet, "/");

        context.addServlet(new ServletHolder(new TaskServlet()), "/listar-task");
        context.addServlet(new ServletHolder(new TaskServlet()), "/criar-task");
        context.addServlet(new ServletHolder(new TaskServlet()), "/editar-task");
        context.addServlet(new ServletHolder(new TaskServlet()), "/deletar-task");

        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(404, "/notfound.html");
        context.setErrorHandler(errorHandler);

        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");

        server.join();
    }
}
