package custommvc.servlet.pages.task;

import custommvc.servlet.annotations.Rota;
import custommvc.servlet.pages.Page;
import h2factory.task.Task;
import h2factory.task.TaskDao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Rota("/editar-task")
public class EditarTaskPage implements Page {
    private final TaskDao taskDao;
    EditarTaskPage(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public String render(HttpServletRequest request, Map<String, Object> parameters) {
        String idStr;
        Task task = null;
        if (parameters.containsKey("id")) {
            idStr = parameters.get("id").toString();
            if (idStr != null) {
                task = taskDao.getById(Integer.parseInt(idStr));
            }
        }

        if (task == null) {
            return "<meta http-equiv='refresh' content='0; url=/listar-task' />";
        }

        if (parameters.containsKey("id") && parameters.containsKey("descricao") && parameters.containsKey("concluido")) {
            String descricao = parameters.get("descricao").toString();
            String concluidoParam = parameters.get("concluido").toString();
            task.setConcluido(Boolean.parseBoolean(concluidoParam));
            if (descricao != null && !descricao.isBlank()) {
                task.setDescricao(descricao);
            }
            taskDao.update(task);
            return "<meta http-equiv='refresh' content='0; url=/custom-mvc/listar-task' />";
        }

        String checkedTrue = task.getConcluido() ? "checked" : "";
        String checkedFalse = !task.getConcluido() ? "checked" : "";

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        String csrfParameterName = csrfToken.getParameterName();
        String csrfTokenValue = csrfToken.getToken();
        return String.format("""
                <!DOCTYPE html>
                <html lang="pt-BR">
                
                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1" />
                    <title>Editar Tarefas</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap');
                
                        body {
                            font-family: 'Inter', sans-serif;
                            background-color: #f5f7fa;
                            margin: 0;
                            padding: 2rem;
                            color: #333;
                            display: flex;
                            align-items: center;
                            flex-direction: column;
                        }
                
                        h1 {
                            text-align: center;
                            margin-bottom: 2rem;
                            font-weight: 600;
                            color: #2c3e50;
                        }
                
                        .task-list {
                            max-width: 600px;
                            margin: 0 auto;
                            background: #fff;
                            border-radius: 10px;
                            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
                            overflow: hidden;
                        }
                
                        .task-item {
                            display: flex;
                            align-items: center;
                            padding: 1rem 1.5rem;
                            border-bottom: 1px solid #eee;
                            transition: background-color 0.25s ease;
                        }
                
                        .task-item:last-child {
                            border-bottom: none;
                        }
                
                        .task-item:hover {
                            background-color: #f0f6ff;
                        }
                
                        .checkbox {
                            margin-right: 1rem;
                            cursor: default;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            width: 24px;
                            height: 24px;
                            border-radius: 5px;
                            background-color: #eee;
                            color: #3498db;
                            font-size: 18px;
                            flex-shrink: 0;
                        }
                
                        .checkbox.completed {
                            background-color: #2ecc71;
                            color: white;
                        }
                
                        .task-desc {
                            flex-grow: 1;
                            font-size: 1.1rem;
                            word-break: break-word;
                        }
                
                        .task-desc.completed {
                            color: #999;
                            text-decoration: line-through;
                            font-style: italic;
                        }
                
                        .task-id {
                            font-size: 0.8rem;
                            color: #bbb;
                            margin-left: 1rem;
                            flex-shrink: 0;
                        }
                
                        .check-icon {
                            width: 18px;
                            height: 18px;
                            fill: currentColor;
                        }
                
                        form.delete-form {
                            margin-left: 1rem;
                        }
                
                        button.delete-btn {
                            background: none;
                            border: none;
                            color: #e74c3c;
                            cursor: pointer;
                            font-size: 1.2rem;
                            line-height: 1;
                        }
                    </style>
                </head>
                
                <body>
                    <a href="/custom-mvc/listar-task" style="text-align: center; text-decoration: none; margin: 1rem auto; display: block; padding: 0.5rem 1rem; font-weight: 600; cursor: pointer; border-radius: 5px; border: 1px solid black; background: #d3d3d3; color: #000; width: 72px;">
                        Tarefas
                    </a>
                    <form method="POST" id="taskForm" style="background:#fff; padding: 2rem; border-radius: 10px; width: 60%%; box-shadow: 0 8px 16px rgba(0,0,0,0.2); font-family: 'Inter', sans-serif;">
                            <h2 style="margin-top:0; font-weight:600; color:#2c3e50; margin-bottom:1rem;">Editar Tarefa</h2>
                            <input type="hidden" name="%s" value="%s" />
                            <label for="id" style="display:block; margin-bottom: 0.5rem; font-weight: 600; color: #333;">Id</label>
                            <input value="%d" readonly id="id" name="id" type="number" required style="width: 100%%; padding: 0.5rem; font-size: 1rem; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 1rem; box-sizing: border-box;" />
                
                            <label for="descricao" style="display:block; margin-bottom: 0.5rem; font-weight: 600; color: #333;">Descrição</label>
                            <input value="%s" id="descricao" name="descricao" type="text" style="width: 100%%; padding: 0.5rem; font-size: 1rem; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 1rem; box-sizing: border-box;" />
                
                            <fieldset style="border:none; padding:0; margin-bottom: 1.5rem;">
                                <legend style="font-weight:600; color:#333; margin-bottom: 0.5rem;">Concluído?</legend>
                                <label style="margin-right: 1rem; cursor: pointer;">
                                    <input type="radio" name="concluido" value="true" %s style="margin-right: 0.25rem;" />
                                    Sim
                                </label>
                                <label style="cursor: pointer;">
                                    <input type="radio" name="concluido" value="false" %s style="margin-right: 0.25rem;" />
                                    Não
                                </label>
                            </fieldset>
                
                            <div style="text-align: right;">
                                <button type="submit" style="background:#3498db; border:none; color:#fff; font-weight:600; padding: 0.5rem 1.5rem; border-radius: 5px; cursor:pointer;">Salvar</button>
                            </div>
                        </form>
                </body>
                
                </html>
                """, csrfParameterName, csrfTokenValue, task.getId(), task.getDescricao(), checkedTrue, checkedFalse);
    }
}
