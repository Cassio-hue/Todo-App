import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import servlet.Servlet;
import servlet.filters.AuthFilter;
import task.TaskDaoJdbc;

import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        TaskDaoJdbc dao = new TaskDaoJdbc();
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SECURITY);
        context.setContextPath("/");
        server.setHandler(context);

        FilterHolder authFilterHolder = new FilterHolder(new AuthFilter());
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        context.addFilter(authFilterHolder, "/*", dispatcherTypes);

        ServletHolder defaultServlet = new ServletHolder(DefaultServlet.class);
        defaultServlet.setInitParameter("resourceBase", "src/main/webapp");
        defaultServlet.setInitParameter("dirAllowed", "true");

        context.addServlet(defaultServlet, "/");
        context.addServlet(new ServletHolder(new Servlet(dao)), "/app");

        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(404, "/notfound.html");
        context.setErrorHandler(errorHandler);

        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }
}
