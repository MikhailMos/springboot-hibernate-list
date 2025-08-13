package ru.example.springboot_hibernate_list.service;

import org.springframework.stereotype.Service;
import ru.example.springboot_hibernate_list.model.exception.ResourceNotFoundException;
import ru.example.springboot_hibernate_list.model.TaskStatus;
import ru.example.springboot_hibernate_list.model.Task;
import ru.example.springboot_hibernate_list.repository.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) throws ResourceNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public Task update(Long id, Task changedTask) {
//        if (taskRepository.existsById(id)) {
//
////            // Checking correct status, if not correct throw error
////            TaskStatus.valueOf(task.getStatus().toUpperCase().replace('-', '_'));
//
//            changedTask.setId(id);
//            if (changedTask.getStatus() == null) {
//                changedTask.setStatus(TaskStatus.TODO);
//            }
//            taskRepository.save(changedTask);
//            return changedTask;
//        }
//
//        throw new ResourceNotFoundException("Task with id " + id + " not found");

        Task foundTask = findById(id); // если метод не выбросил ошибку, значит задача есть
        foundTask.copyWithoutId(changedTask);

        taskRepository.save(foundTask);

        return foundTask;
    }

    @Override
    public Task update(Long id, TaskStatus newStatus) {

        // TODO: добавить транзактивности
        // TODO: что-то было про Patch-запросы...

        Task task = findById(id);
        task.setStatus(newStatus);
        taskRepository.save(task);

        return task;
    }

    @Override
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with id " + id + " not found");
        }

        taskRepository.deleteById(id);
    }

}
