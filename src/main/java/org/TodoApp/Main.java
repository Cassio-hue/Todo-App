package org.TodoApp;

import daoAPI.GenericDAO;
import database.DAOManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import task.Task;
import task.TaskDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
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
            DAOManager con = new DAOManager();
            try {
                con.open();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            GenericDAO<Task> task = new TaskDao(con.getConnection());
            List<Task> tasks = task.getAll();

            String taskCard = "";
            for (Task t : tasks) {
                if (t.getConcluida()) {
                    taskCard += """
                            <div class="task-card done">
                                <div class="task-desc">""" + t.getDescricao() + """
                                </div>
                                <div class="task-actions">
                                    <button class="done-btn" disabled>Concluído</button>
                                    <button class="edit-btn">Editar</button>
                                </div>
                            </div>""";
                } else {
                    taskCard += """
                            <div class="task-card">
                                <div class="task-desc">""" + t.getDescricao() + """
                                </div>
                                <div class="task-actions">
                                    <button class="done-btn">Marcar como feito</button>
                                    <button class="edit-btn">Editar</button>
                                </div>
                            </div>
                            """;
                }
            }


            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("""
                    <!DOCTYPE html>
                           <html lang="pt-BR">
                           <head>
                               <meta charset="UTF-8" />
                               <meta name="viewport" content="width=device-width, initial-scale=1" />
                               <title>Lista de Tasks</title>
                               <style>
                                   body {
                                       font-family: Arial, sans-serif;
                                       background-color: #f5f5f5;
                                       padding: 20px;
                                   }
                                   h1 {
                                       text-align: center;
                                       margin-bottom: 30px;
                                   }
                                   .task-list {
                                       display: flex;
                                       flex-direction: column;
                                       gap: 15px;
                                       max-width: 600px;
                                       margin: 0 auto;
                                   }
                                   .task-card {
                                       background-color: white;
                                       border-radius: 8px;
                                       padding: 15px 20px;
                                       box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                                       display: flex;
                                       justify-content: space-between;
                                       align-items: center;
                                       transition: background-color 0.3s ease;
                                   }
                                   .task-card.done {
                                       background-color: #d4edda;
                                       text-decoration: line-through;
                                       color: #6c757d;
                                   }
                                   .task-desc {
                                       flex: 1;
                                       font-size: 1.1rem;
                                   }
                                   .task-actions button {
                                       background-color: #007bff;
                                       border: none;
                                       color: white;
                                       padding: 8px 12px;
                                       margin-left: 10px;
                                       border-radius: 4px;
                                       cursor: pointer;
                                       font-size: 0.9rem;
                                       transition: background-color 0.2s ease;
                                   }
                                   .task-actions button:hover {
                                       background-color: #0056b3;
                                   }
                                   .task-actions button.done-btn {
                                       background-color: #28a745;
                                   }
                                   .task-actions button.done-btn:hover {
                                       background-color: #1e7e34;
                                   }
                               </style>
                           </head>
                           <body>
                    
                               <h1>Lista de Tasks</h1>
                    
                               <div class="task-list">
                    """ + taskCard + """
                                   </div>
                               </body>
                           </html>
                    """);
        }
    }
}