package ru.example.springboot.hibernate.list.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.example.springboot.hibernate.list.model.exception.ResourceNotFoundException;
import ru.example.springboot.hibernate.list.model.TaskStatus;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.repository.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    private final ObjectMapper objectMapper;

    public TaskServiceImpl(TaskRepository taskRepository,
                           ObjectMapper objectMapper) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
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

       Task foundTask = findById(id).copyWithoutId(changedTask);

        return taskRepository.save(foundTask);
    }

    @Override
    @Transactional
    public Task update(Long id, TaskStatus newStatus) throws ResourceNotFoundException {

        Task task = findById(id);
        task.setStatus(newStatus);

        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task update(Long id, JsonPatch patch) {
        Task task = findById(id);

        try {
            Task taskPatched = applyPatchToTask(patch, task);
            return taskRepository.save(taskPatched);
        } catch (JsonPatchException | JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with id " + id + " not found");
        }

        taskRepository.deleteById(id);
    }

    private Task applyPatchToTask(JsonPatch patch, Task targetTask)  throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetTask, JsonNode.class));
        return objectMapper.treeToValue(patched, Task.class);
    }

}
