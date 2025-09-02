package springmvc.controllers;

import h2factory.task.Task;
import h2factory.task.TaskDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskController {

    private final TaskDao taskRepository;
    TaskController(TaskDao taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/listar-task")
    public String listarTasks(Model model) {
        model.addAttribute("tasks", taskRepository.list());
        return "listar-task";
    }

    @GetMapping("/criar-task")
    public String criarTask(Model model) {
        model.addAttribute("task", new Task());
        return  "criar-task";
    }

    @PostMapping("/criar-task")
    public String criarTask(@ModelAttribute Task task) {
        taskRepository.insert(task);
        return "redirect:/spring-mvc/listar-task";
    }

    @GetMapping("/editar-task")
    public String editarTask(@RequestParam("id") int id, Model model) {
        Task task = taskRepository.getById(id);
        model.addAttribute("task", task);
        return "editar-task";
    }

    @PostMapping("/editar-task")
    public String editarTask(@ModelAttribute Task task) {
        taskRepository.update(task);
        return "redirect:/spring-mvc/listar-task";
    }

    @PostMapping("/deletar-task")
    public String deletarTask(@RequestParam("id") int id) {
        taskRepository.delete(id);
        return "redirect:/spring-mvc/listar-task";
    }
}