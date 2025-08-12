package ru.example.springboot_hibernate_list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot_hibernate_list.model.Task;
import ru.example.springboot_hibernate_list.service.TaskService;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody Task task) {

        try {
            final Task updatedTask = taskService.update(id, task);

            return updatedTask == null
                    ? new ResponseEntity<>("Task not found by id", HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(updatedTask, HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/tasks/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id, @RequestBody String request) {

        String statusStr;

        try {
            statusStr = taskService.getStatusFromJson(request);
            if (statusStr == null) {
                return new ResponseEntity<>("Can't find node \"status\"", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final Task updatedTask = taskService.update(id, statusStr);

        return updatedTask == null
                ? new ResponseEntity<>("Task not found by id", HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable("id") Long id) {

        taskService.deleteById(id);

    }

}
