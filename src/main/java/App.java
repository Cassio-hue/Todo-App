import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlet.CriarTaskServlet;
import servlet.DeletarTaskServlet;
import servlet.EditarTaskServlet;
import servlet.ListarTaskServlet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Registrar servlets específicos em seus paths
        context.addServlet(ListarTaskServlet.class, "/listar-task");
        context.addServlet(CriarTaskServlet.class, "/criar-task");
        context.addServlet(EditarTaskServlet.class, "/editar-task");
        context.addServlet(DeletarTaskServlet.class, "/deletar-task");

        server.setHandler(context);
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
        server.join();
    }
}
