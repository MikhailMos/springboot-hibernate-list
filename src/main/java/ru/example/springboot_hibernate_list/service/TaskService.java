package ru.example.springboot_hibernate_list.service;

import ru.example.springboot_hibernate_list.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> findAll();
    Task save(Task task);
    Task findById(Long id);
    Task update(Long id, Task task) throws IllegalArgumentException;
    Task update(Long id, String status);
    void deleteById(Long id);
    String getStatusFromJson(String request) throws IllegalArgumentException, Exception;
}
