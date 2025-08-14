package ru.example.springboot_hibernate_list.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.example.springboot_hibernate_list.model.exception.ResourceNotFoundException;
import ru.example.springboot_hibernate_list.model.Task;
import ru.example.springboot_hibernate_list.model.TaskStatus;

import java.util.List;

@Validated
public interface TaskService {

    List<Task> findAll();
    Task save(@Valid Task task);
    Task findById(Long id);
    Task update(Long id, @Valid Task changedTask);
    Task update(Long id, TaskStatus newStatus);
    void deleteById(Long id);

}
