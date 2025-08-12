package ru.example.springboot_hibernate_list.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot_hibernate_list.model.Task;
import ru.example.springboot_hibernate_list.sevice.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TaskController {

    private final TaskService taskService;

    //@Autowired //??? не знаю нужно ли тут эту аннотацию?
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {

        System.out.println("Show all tasks");

        final List<Task> tasks = taskService.readAll();

        return tasks != null && !tasks.isEmpty()
                ? new ResponseEntity<>(tasks, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> CreateTask(@RequestBody Task task) {

        taskService.create(task);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") Long id) {

        final Task task = taskService.read(id);

        return task == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(task, HttpStatus.OK);

    }

    @PutMapping("/update/{id}")
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

    @PutMapping("/tasks/{id}")
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
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") Long id) {

        final boolean deleted = taskService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
