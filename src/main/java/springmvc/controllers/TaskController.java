package springmvc.controllers;

import h2factory.task.Task;
import h2factory.task.TaskDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/spring-mvc")
public class TaskController {

    private final TaskDao taskDao;

    TaskController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @GetMapping("/listar-task")
    public String listarTasks(Model model) {
        model.addAttribute("tasks", taskDao.list());
        return "listar-task";
    }

    @GetMapping("/criar-task")
    public String criarTask(Model model) {
        model.addAttribute("task", new Task());
        return "criar-task";
    }

    @PostMapping("/criar-task")
    public String criarTask(@ModelAttribute Task task) {
        taskDao.insert(task);
        return "redirect:/spring-mvc/listar-task";
    }

    @GetMapping("/editar-task")
    public String editarTask(@RequestParam("id") int id, Model model) {
        Task task = taskDao.getById(id);
        model.addAttribute("task", task);
        return "editar-task";
    }

    @PostMapping("/editar-task")
    public String editarTask(@ModelAttribute Task task) {
        taskDao.update(task);
        return "redirect:/spring-mvc/listar-task";
    }

    @PostMapping("/deletar-task")
    public String deletarTask(@RequestParam("id") int id) {
        taskDao.delete(id);
        return "redirect:/spring-mvc/listar-task";
    }
}