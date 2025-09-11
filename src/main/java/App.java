import custommvc.servlet.MiniServletMVC;
import jakarta.servlet.DispatcherType;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import springmvc.AppConfig;
import wicket.WicketApplication;

import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();

        webContext.register(AppConfig.class);
        webContext.setServletContext(contextHandler.getServletContext());
        webContext.refresh();

        contextHandler
                .addFilter(DelegatingFilterProxy.class, "/*", null)
                .setInitParameter("targetBeanName", "springSecurityFilterChain");
        contextHandler.addEventListener(new ContextLoaderListener(webContext));

        WicketApplication wicketApplication = webContext.getBean(WicketApplication.class);
        FilterHolder wicketFilterHolder = new FilterHolder(WicketFilter.class);
        wicketFilterHolder.setInitParameter("applicationClassName", wicketApplication.getClass().getName());
        wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/wicket/*");
        contextHandler.addFilter(wicketFilterHolder, "/wicket/*", EnumSet.of(DispatcherType.REQUEST));

        MiniServletMVC miniServlet = webContext.getBean(MiniServletMVC.class);
        contextHandler.addServlet(new ServletHolder(miniServlet), "/custom-mvc/*");
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(webContext)), "/*");

        server.setHandler(contextHandler);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }
}
