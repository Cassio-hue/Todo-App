package servlet.pages;

import custom.annotations.Rota;

import java.util.Map;

@Rota("/")
public class HomePage implements Page {
    public String render(Map<String, Object> parameters) {
        return """
                    <!DOCTYPE html>
                    <html lang="pt-BR">
                    <head>
                        <meta charset="UTF-8"/>
                        <title>TodoApp</title>
                    </head>
                    <body style="text-align: center;">
                        <h1>TodoApp</h1>
                        <div style="display: flex; flex-direction: column; justify-content: space-around;">
                            <a href="/listar-task">MiniServlet MVC</a>
                        </div>
                    </body>
                    </html>
                """;
    }
}
