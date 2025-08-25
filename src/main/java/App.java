import custom.annotations.Rota;
import io.github.classgraph.*;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import servlet.TaskServlet;
import servlet.filters.AuthFilter;
import servlet.pages.ListarTaskPage;
import task.TaskDaoJdbc;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        for (String route : getRoutes()) {
            context.addServlet(new ServletHolder(new TaskServlet(dao)), route);
        }

        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(404, "/notfound.html");
        context.setErrorHandler(errorHandler);

        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }

    private static List<String> getRoutes() {
        List<String> routes = new ArrayList<>();

        try (ScanResult scanResult =
                     new ClassGraph()
                             .verbose()
                             .enableAllInfo()
                             .scan()) {
            for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(Rota.class.getName())) {
                AnnotationInfo routeAnnotationInfo = routeClassInfo.getAnnotationInfo(Rota.class.getName());
                List<AnnotationParameterValue> routeParamVals = routeAnnotationInfo.getParameterValues();
                routes.add((String) routeParamVals.getFirst().getValue());
            }
        } catch (Exception ex) {
            Logger.getLogger(ListarTaskPage.class.getName()).log(Level.SEVERE, "Erro ao listar rotas", ex);
        }
        return routes;
    }
}
