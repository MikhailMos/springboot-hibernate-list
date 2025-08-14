package ru.example.springboot.hibernate.list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {

        return taskService.findAll();

    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {

        return taskService.save(task);

    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable("id") Long id) {

        return taskService.findById(id);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable("id") Long id, @RequestBody Task changedTask) {

        return taskService.update(id, changedTask);

    }

    @PutMapping("/tasks/{id}/status")
    public Task updateStatus(@PathVariable("id") Long id, @RequestBody Task changedTask) {

        return taskService.update(id, changedTask.getStatus());

    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable("id") Long id) {

        taskService.deleteById(id);

    }

}
