package pages.task;

import task.Task;

import java.util.List;

public class ListarTaskPage {
    public String render(List<Task> tasks) {
        StringBuilder tarefas = new StringBuilder();
        for (Task t : tasks) {
            int id = t.getId();
            String desc = t.getDescricao();
            boolean concluido = t.getConcluido();

            if (concluido) {
                tarefas.append(String.format("""
                                <div class="task-item">
                                    <div class="checkbox completed" title="Concluído">
                                      <svg onclick="this.closest('form').submit()" class="check-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M9 16.2l-3.5-3.5 1.41-1.41L9 13.38l7.09-7.09L17.5 7l-8.5 8.5z"></path>
                                      </svg>
                                    </div>
                                    <div class="task-desc completed">%s</div>
                                    <div class="task-id">#%s</div>
                                </div>
                        """, desc, id));
            } else {
                tarefas.append(String.format("""
                            <div class="task-item">
                                <div class="checkbox" title="Pendente">
                                  <svg onclick="this.closest('form').submit()" class="check-icon" viewBox="0 0 24 24" fill="none" stroke="#3498db" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
                                    <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                                  </svg>
                                </div>
                                <div class="task-desc">%s</div>
                                <div class="task-id">#%s</div>
                            </div>
                        """, desc, id));
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
                    <h1>Minhas Tarefas</h1>
                    <button id="openModalBtn" style="margin: 1rem auto; display: block; padding: 0.5rem 1rem; font-weight: 600; cursor:pointer; border-radius:5px; border:none; background:#3498db; color:#fff;">
                        Nova Tarefa
                    </button>
                
                    <div class="task-list">
                        %s
                    </div>
                
                    <div id="taskModal" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.5); backdrop-filter: blur(2px); z-index:1000; align-items:center; justify-content:center;">
                        <form id="taskForm" style="background:#fff; padding: 2rem; border-radius: 10px; width: 90%%; max-width: 400px; box-shadow: 0 8px 16px rgba(0,0,0,0.2); font-family: 'Inter', sans-serif;">
                            <h2 style="margin-top:0; font-weight:600; color:#2c3e50; margin-bottom:1rem;">Nova Tarefa</h2>
                            <input id="formAction" type="hidden" name="action" />
                            <label for="descricao" style="display:block; margin-bottom: 0.5rem; font-weight: 600; color: #333;">Descrição</label>
                            <input id="descricao" name="descricao" type="text" required style="width: 100%%; padding: 0.5rem; font-size: 1rem; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 1rem; box-sizing: border-box;" />
                
                            <fieldset style="border:none; padding:0; margin-bottom: 1.5rem;">
                                <legend style="font-weight:600; color:#333; margin-bottom: 0.5rem;">Concluído?</legend>
                                <label style="margin-right: 1rem; cursor: pointer;">
                                    <input type="radio" name="concluido" value="true" style="margin-right: 0.25rem;" />
                                    Sim
                                </label>
                                <label style="cursor: pointer;">
                                    <input type="radio" name="concluido" value="false" checked style="margin-right: 0.25rem;" />
                                    Não
                                </label>
                            </fieldset>
                
                            <div style="text-align: right;">
                                <button type="button" id="cancelBtn" style="background:none; border:none; color:#999; font-weight:600; margin-right: 1rem; cursor:pointer;">Cancelar</button>
                                <button type="submit" style="background:#3498db; border:none; color:#fff; font-weight:600; padding: 0.5rem 1.5rem; border-radius: 5px; cursor:pointer;">Salvar</button>
                            </div>
                        </form>
                    </div>
                
                    <script>
                        const modal = document.getElementById('taskModal');
                        const openBtn = document.getElementById('openModalBtn');
                        const cancelBtn = document.getElementById('cancelBtn');
                        const form = document.getElementById('taskForm');
                        const formAction = document.getElementById('formAction');
                
                        openBtn.addEventListener('click', () => {
                            modal.style.display = 'flex';
                            formAction.value = "create";
                        });
                
                        cancelBtn.addEventListener('click', () => {
                            modal.style.display = 'none';
                            form.reset();
                        });
                
                        modal.addEventListener('click', (e) => {
                            if (e.target === modal) {
                                modal.style.display = 'none';
                                form.reset();
                            }
                        });
                
                        form.addEventListener('submit', (e) => {
                            e.preventDefault();
                            form.action = "/criar-task"
                            form.method = "post"
                            form.submit();
                            modal.style.display = 'none';
                            setTimeout(() => form.reset(), 100);
                        });
                    </script>
                </body>
                
                </html>
                """, tarefas);
    }
}
