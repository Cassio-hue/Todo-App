package Task;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TaskServlet extends HttpServlet {
    private final List<Task> tasks;

    public TaskServlet(List<Task> tasks) {
        this.tasks = tasks;
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"pt-BR\">");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\" />");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />");
            out.println("<title>Lista de Tarefas</title>");
            out.println("<style>");
            out.println("  @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap');");
            out.println("  body {font-family: 'Inter', sans-serif; background-color: #f5f7fa; margin: 0; padding: 2rem; color: #333;}");
            out.println("  h1 {text-align: center; margin-bottom: 2rem; font-weight: 600; color: #2c3e50;}");
            out.println("  .task-list {max-width: 600px; margin: 0 auto; background: #fff; border-radius: 10px; box-shadow: 0 8px 16px rgba(0,0,0,0.1); overflow: hidden;}");
            out.println("  .task-item {display: flex; align-items: center; padding: 1rem 1.5rem; border-bottom: 1px solid #eee; transition: background-color 0.25s ease;}");
            out.println("  .task-item:last-child {border-bottom: none;}");
            out.println("  .task-item:hover {background-color: #f0f6ff;}");
            out.println("  .checkbox {margin-right: 1rem; cursor: default; display: flex; align-items: center; justify-content: center; width: 24px; height: 24px; border-radius: 5px; background-color: #eee; color: #3498db; font-size: 18px; flex-shrink: 0;}");
            out.println("  .checkbox.completed {background-color: #2ecc71; color: white;}");
            out.println("  .task-desc {flex-grow: 1; font-size: 1.1rem; word-break: break-word;}");
            out.println("  .task-desc.completed {color: #999; text-decoration: line-through; font-style: italic;}");
            out.println("  .task-id {font-size: 0.8rem; color: #bbb; margin-left: 1rem; flex-shrink: 0;}");
            out.println("  .check-icon {width: 18px; height: 18px; fill: currentColor;}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Minhas Tarefas</h1>");
            out.println("<div class=\"task-list\">");

            for (Task t : tasks) {
                boolean done = Boolean.TRUE.equals(t.getConcluido());
                out.println("<div class=\"task-item\">");

                if (done) {
                    out.println("<div class=\"checkbox completed\" title=\"Concluído\">");
                    out.println("<svg class=\"check-icon\" viewBox=\"0 0 24 24\" xmlns=\"http://www.w3.org/2000/svg\">");
                    out.println("<path d=\"M9 16.2l-3.5-3.5 1.41-1.41L9 13.38l7.09-7.09L17.5 7l-8.5 8.5z\"/>");
                    out.println("</svg>");
                    out.println("</div>");
                    out.println("<div class=\"task-desc completed\">" + escapeHtml(t.getDescricao()) + "</div>");
                } else {
                    out.println("<div class=\"checkbox\" title=\"Pendente\">");
                    out.println("<svg class=\"check-icon\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"#3498db\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" xmlns=\"http://www.w3.org/2000/svg\">");
                    out.println("<rect x=\"3\" y=\"3\" width=\"18\" height=\"18\" rx=\"2\" ry=\"2\"/>");
                    out.println("</svg>");
                    out.println("</div>");
                    out.println("<div class=\"task-desc\">" + escapeHtml(t.getDescricao()) + "</div>");
                }

                out.println("<div class=\"task-id\">#" + t.getId() + "</div>");
                out.println("</div>");
            }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
