package ru.example.springboot_hibernate_list.service;

import ru.example.springboot_hibernate_list.exception.ResourceNotFoundException;
import ru.example.springboot_hibernate_list.model.Task;
import ru.example.springboot_hibernate_list.model.TaskStatus;

import java.util.List;

public interface TaskService {

    List<Task> findAll();
    Task save(Task task);
    Task findById(Long id) throws ResourceNotFoundException;
    Task update(Long id, Task changedTask);
    Task update(Long id, TaskStatus newStatus);
    void deleteById(Long id);

}
