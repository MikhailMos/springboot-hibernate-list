package ru.example.springboot_hibernate_list.sevice;

import ru.example.springboot_hibernate_list.entity.TaskStatus;
import ru.example.springboot_hibernate_list.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> readAll();
    void create(Task task);
    Task read(Long id);
    Task update(Long id, Task task) throws IllegalArgumentException;
    Task update(Long id, String status);
    boolean delete(Long id);
    String getStatusFromJson(String request) throws IllegalArgumentException, Exception;
}
