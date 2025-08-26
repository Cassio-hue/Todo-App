package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import task.Task;
import task.TaskDaoJdbc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniServletMVC extends HttpServlet {
//    TaskDaoJdbc dao;
//
//    public MiniServletMVC(TaskDaoJdbc dao) {
//        this.dao = dao;
//    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        switch (uri) {
            case "/listar-task":
                RequestDispatcher listarDispatcher = request.getRequestDispatcher("/listar-task");
                listarDispatcher.forward(request, response);
                break;
            case "/criar-task":
                RequestDispatcher criarDispatcher = request.getRequestDispatcher("/criar-task");
                criarDispatcher.forward(request, response);
                break;
            case "/editar-task":
                RequestDispatcher editarDispatcher = request.getRequestDispatcher("/editar-task");
                editarDispatcher.forward(request, response);
                break;
            case "/deletar-task":
                RequestDispatcher deletarDispatcher = request.getRequestDispatcher("/deletar-task");
                deletarDispatcher.forward(request, response);
                break;
            default:
                response.sendError(404);
                break;
        }
    }
}
