package ru.example.springboot.hibernate.list.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.model.TaskStatus;
import ru.example.springboot.hibernate.list.model.exception.ResourceNotFoundException;
import ru.example.springboot.hibernate.list.repository.TaskRepository;

import java.util.List;

/**
 * Класс содержит основную логику обработки CRUD методов.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    /**
     * Предоставляет встроенные методы для CRUD-операций.
     *
     */
    private final TaskRepository taskRepository;

    /**
     * Используется для сериализации и десериализации объектов Java в JSON
     * и наоборот.
     *
     */
    private final ObjectMapper objectMapper;

    /**
     * Возвращает список со всеми задачами.
     * Только для транзакций чтения.
     *
     * @return список задач
     */
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByUserUsername(String username) {
        return taskRepository.findByUserUsername(username);
    }

    /**
     * Сохраняет задачу в базу данных.
     *
     * @param task  задача, которую нужно сохранить
     * @return сохраненная задача
     */
    @Transactional(readOnly = false)
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Возвращает задачу найденную по идентификатору.
     * Только для транзакций чтения.
     *
     * @param id    числовой идентификатор задачи, которую нужно найти
     * @return      найденная задача
     */
    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    /**
     * Обновляет задачу новыми данными. При этом задача перезаписывается полностью.
     *
     * @param id          числовой идентификатор задачи, которую нужно изменить
     * @param changedTask задача содержащая измененные данные
     * @return            измененная задача
     * @throws ResourceNotFoundException если задача не была найдена по идентификатору
     */
    @Transactional
    public Task update(Long id, Task changedTask) throws ResourceNotFoundException {

        Task foundTask = findById(id).copyWithoutId(changedTask);

        return taskRepository.save(foundTask);
    }

    /**
     * Обновляет статус задачи.  При этом задача перезаписывается полностью.
     *
     * @param id        числовой идентификатор задачи, статус которой нужно изменить
     * @param newStatus новый статус
     * @return          измененная задача
     * @throws ResourceNotFoundException если задача не была найдена по идентификатору
     */
    @Transactional
    public Task update(Long id, TaskStatus newStatus) throws ResourceNotFoundException {

        Task task = findById(id);
        task.setStatus(newStatus);

        return taskRepository.save(task);
    }

    /**
     * Обновляет статус задачи. Задача обновляется частично, благодаря JsonPatch.
     *
     * @param id    числовой идентификатор задачи, статус которой нужно изменить
     * @param patch список операций, которые нужно последовательно применить к целевому объекту
     * @return      задача с измененным статусом
     */
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

    /**
     * Удаляет задачу по идентификатору задачи.
     *
     * @param id    числовой идентификатор задачи, которую нужно удалить
     */
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
