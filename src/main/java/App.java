import custommvc.servlet.MiniServletMVC;
import custommvc.servlet.filters.AuthFilter;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import springmvc.WebConfig;

import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Recursos estáticos. Página inicial padrão.
        ServletHolder staticHolder = new ServletHolder(DefaultServlet.class);
        staticHolder.setInitParameter("resourceBase", "src/main/webapp");
        staticHolder.setInitParameter("dirAllowed", "true");
        context.addServlet(staticHolder, "/");

        // Filtro de Autenticação
        FilterHolder authFilterHolder = new FilterHolder(new AuthFilter());
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        context.addFilter(authFilterHolder, "/*", dispatcherTypes);

        context.addServlet(new ServletHolder(new MiniServletMVC()), "/custom-mvc/*");

        // Config DispatcherServlet para o Spring
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);
        ServletHolder springMvc = new ServletHolder(new DispatcherServlet(webContext));
        context.addServlet(springMvc, "/spring-mvc/*");

        server.setHandler(context);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }
}
