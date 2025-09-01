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
import springmvc.AppConfig;

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

        // Config DispatcherServlet para o Spring
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(AppConfig.class);
        webContext.setServletContext(context.getServletContext()); // FIX: exception with message: No ServletContext set at org.springframework.beans.factory.support.ConstructorResolver
        webContext.refresh();                                      // FIX: BeanFactory not initialized or already closed - call 'refresh' before accessing beans via the ApplicationContext

        MiniServletMVC miniServlet = webContext.getBean(MiniServletMVC.class);
        context.addServlet(new ServletHolder(miniServlet), "/custom-mvc/*");

        ServletHolder springMvc = new ServletHolder(new DispatcherServlet(webContext));
        context.addServlet(springMvc, "/spring-mvc/*");

        server.setHandler(context);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }
}
