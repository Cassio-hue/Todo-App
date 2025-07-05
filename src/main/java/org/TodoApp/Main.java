package org.TodoApp;

import daoAPI.DaoAPI;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import task.Task;
import task.TaskDao;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        DaoAPI<Task> taskDao = new TaskDao();
        List<Task> tasks = taskDao.getAll();
        Task newTast = new  Task(3, "Usando a DAO API para salvar a task");
        taskDao.save(newTast);
        tasks.forEach(task -> System.out.println(task.getDescricao()));

        // Porta do servidor
        int port = 8080;
        Server server = new Server(port);

        // Contexto
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Add um servlet de Hello World
        context.addServlet(HtmlServlet.class, "/");

        server.setHandler(context);

        server.start();
        System.out.println("Servidor rodando em http://localhost:" + port + "/");
        server.join();
    }

    public static class HtmlServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("""
                <!DOCTYPE html>
                <html>
                  <head>
                    <title>Minha Página</title>
                  </head>
                  <body>
                    <h1>Olá Mundo em HTML!</h1>
                  </body>
                </html>
                """);
        }
    }
}