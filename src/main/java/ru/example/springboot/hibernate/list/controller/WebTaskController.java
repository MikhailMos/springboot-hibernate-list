package ru.example.springboot.hibernate.list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.service.TaskService;

@Controller
public class WebTaskController {

    private final TaskService taskService;

    public WebTaskController(@Autowired TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String addTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "add-task";
    }

    @PostMapping("/add")
    public String addTask(Model model,
                          @ModelAttribute("task") Task task) {

        Task savedTask = taskService.save(task);
        return "redirect:/index";
    }
}
