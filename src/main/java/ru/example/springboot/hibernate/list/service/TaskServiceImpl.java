package ru.example.springboot.hibernate.list.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.example.springboot.hibernate.list.model.exception.ResourceNotFoundException;
import ru.example.springboot.hibernate.list.model.TaskStatus;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.repository.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = false)
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Task update(Long id, Task changedTask) throws ResourceNotFoundException {

        // TODO: что-то было про Patch-запросы...

        Task foundTask = findById(id).copyWithoutId(changedTask);

        return taskRepository.save(foundTask);
    }

    @Override
    @Transactional
    public Task update(Long id, TaskStatus newStatus) throws ResourceNotFoundException {

        // TODO: что-то было про Patch-запросы...

        Task task = findById(id);
        task.setStatus(newStatus);

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with id " + id + " not found");
        }

        taskRepository.deleteById(id);
    }

}
