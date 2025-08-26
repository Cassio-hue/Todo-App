import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.MiniServletMVC;
import servlet.filters.AuthFilter;

import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Filtro de Autenticação
        FilterHolder authFilterHolder = new FilterHolder(new AuthFilter());
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        context.addFilter(authFilterHolder, "/*", dispatcherTypes);

        context.addServlet(new ServletHolder(new MiniServletMVC()), "/*");

        server.setHandler(context);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }
}
