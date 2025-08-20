package ru.example.springboot.hibernate.list.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.example.springboot.hibernate.list.model.exception.ResourceNotFoundException;
import ru.example.springboot.hibernate.list.model.TaskStatus;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.repository.TaskRepository;

import java.util.List;

/**
 * Класс содержит основную логику обработки CRUD методов.
 * @see TaskService
 */
@Service
public class TaskServiceImpl implements TaskService{

    /**
     * Предоставляет встроенные методы для CRUD-операций.
     *
     * @see TaskRepository
     */
    private final TaskRepository taskRepository;

    /**
     * Используется для сериализации и десериализации объектов Java в JSON
     * и наоборот.
     *
     * @see ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * Создает экземпляр TaskServiceImpl с внедрением репозитория.
     *
     * @param taskRepository репозиторий, который будет использоваться
     * @param objectMapper   класс ObjectMapper библиотеки Jackson, который будет использоваться
     */
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

    /**
     * Обновляет данные задачи ({@code targetTask}) по массиву операций ({@code patch}).
     *
     * @param patch         массив операций, которые нужно последовательно применить к целевому объекту
     * @param targetTask    задача, данные которой нужно обновить
     * @return              задача с обновленными данными
     * @throws JsonPatchException       если некоторые атрибуты в фактическом JSON отсутствуют
     * @throws JsonProcessingException  если в процессе сериализации/десериализации возникла ошибка
     */
    private Task applyPatchToTask(JsonPatch patch, Task targetTask)  throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetTask, JsonNode.class));
        return objectMapper.treeToValue(patched, Task.class);
    }

}
