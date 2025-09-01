package custommvc.servlet.pages.task;

import custommvc.servlet.annotations.Rota;
import custommvc.servlet.pages.Page;
import h2factory.task.Task;
import h2factory.task.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Rota("/listar-task")
public class ListarTaskPage implements Page {
    @Qualifier("taskDaoJdbc")
    @Autowired
    TaskDao taskDaoJdbc;

    public String render(Map<String, Object> parameters) {
        List<Task> tasks = taskDaoJdbc.list();
        StringBuilder tarefas = new StringBuilder();

        for (Task t : tasks) {
            int id = t.getId();
            String desc = t.getDescricao();
            boolean concluido = t.getConcluido();

            if (concluido) {
                tarefas.append(String.format("""
                                <div id="taskItem" class="task-item">
                                    <div class="checkbox completed" title="Concluído">
                                      <svg class="check-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M9 16.2l-3.5-3.5 1.41-1.41L9 13.38l7.09-7.09L17.5 7l-8.5 8.5z"></path>
                                      </svg>
                                    </div>
                                    <div class="task-desc completed">%s</div>
                                    <form method="GET" action="/custom-mvc/editar-task" style="margin-right: 8px;">
                                        <input type="hidden" name="id" value="%s"/>
                                        <button type="submit">Editar</button>
                                    </form>
                                    <form method="POST" action="/custom-mvc/deletar-task">
                                        <input type="hidden" name="id" value="%s"/>
                                        <button type="submit">Excluir</button>
                                    </form>
                                </div>
                        """, desc, id, id));
            } else {
                tarefas.append(String.format("""
                            <div id="taskItem" class="task-item">
                                <div class="checkbox" title="Pendente">
                                  <svg class="check-icon" viewBox="0 0 24 24" fill="none" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
                                    <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                                  </svg>
                                </div>
                                <div class="task-desc">%s</div>
                                <form method="GET" action="/custom-mvc/editar-task" style="margin-right: 8px;">
                                    <input type="hidden" name="id" value="%s"/>
                                    <button type="submit">Editar</button>
                                </form>
                                <form method="POST" action="/custom-mvc/deletar-task">
                                    <input type="hidden" name="id" value="%s"/>
                                    <button type="submit">Excluir</button>
                                </form>
                            </div>
                        """, desc, id, id));
            }
        }

        return String.format("""
                <!DOCTYPE html>
                <html lang="pt-BR">
                
                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1" />
                    <title>Lista de Tarefas</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap');
                
                        body {
                            font-family: 'Inter', sans-serif;
                            background-color: #f5f7fa;
                            margin: 0;
                            padding: 2rem;
                            color: #333;
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
                    </style>
                </head>
                
                <body>
                    <h1>Minhas Tarefas</h1>
                    <a href="/custom-mvc/criar-task" style="display: flex; justify-content: center; padding: 16px;">Criar Tarefas</a>
                    <div class="task-list">
                        %s
                    </div>
                </body>
                </html>
                """, tarefas);
    }
}
