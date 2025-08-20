import task.Task;
import task.TaskDAO;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String password = "";

        try (Connection conn = DriverManager.getConnection(jdbcURL, user, password)) {

            TaskDAO taskDAO = new TaskDAO(conn);
            taskDAO.criarTabela();

            taskDAO.add(new Task("Fazer o AppTodo"));
            taskDAO.add(new Task("Limpar a casa"));
            taskDAO.add(new Task("Levar o cachorro para passear"));

            List<Task> tasks = taskDAO.list();
            System.out.println("Lista de Tarefas:");
            for (Task task : tasks) {
                System.out.println(task);
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Qual tarefa você quer editar: ");
            int numero = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            System.out.print("Concluído (0 | 1): ");
            int concluido = scanner.nextInt();

            boolean taskUpdated = taskDAO.update(new Task(numero, descricao, concluido == 1));
            if (taskUpdated) {
                System.out.println(taskDAO.list(numero));
            } else {
                System.out.println("Não foi possível atualizar a task.");
            }

            System.out.print("Qual tarefa você quer excluir: ");
            numero = scanner.nextInt();
            scanner.close();

            taskDAO.delete(numero);

            tasks = taskDAO.list();
            System.out.println("Lista de Tarefas:");
            for (Task task : tasks) {
                System.out.println(task);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}