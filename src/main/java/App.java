import custommvc.servlet.MiniServletMVC;
import custommvc.servlet.filters.AuthFilter;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import springmvc.AppConfig;

import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(AppConfig.class);
        webContext.setServletContext(contextHandler.getServletContext());
        webContext.refresh();

        FilterHolder authFilterHolder = new FilterHolder(new AuthFilter());
        contextHandler.addFilter(authFilterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

        MiniServletMVC miniServlet = webContext.getBean(MiniServletMVC.class);
        contextHandler.addServlet(new ServletHolder(miniServlet), "/custom-mvc/*");
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(webContext)), "/*");
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(webContext)), "/spring-mvc/*");

        server.setHandler(contextHandler);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }
}
