package ru.example.springboot.hibernate.list.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot.hibernate.list.mapper.TaskMapper;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.model.TaskDto;
import ru.example.springboot.hibernate.list.service.TaskService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST контроллер для управления задачами.
 * Обрабатывает HTTP-запросы, связанные с созданием, чтением, обновлением и удалением задач.
 */
@RestController
@RequestMapping("/${root-api-mapping.path}")
@RequiredArgsConstructor
public class TaskRestController {

    /**
     * Интерфейс содержащий логику конвертации задач
     */
    private final TaskMapper taskMapper;

    /**
     * Экземпляр класса содержащий логику обработки данных
     *
     * @see TaskService
     */
    private final TaskService taskService;

    /**
     * Возвращает список всех задач.
     *
     * @return список объектов Task
     */
    @GetMapping("/tasks")
    public List<TaskDto> getAllTasks() {

        return taskService.findAll()
                .stream()
                .map(task -> new TaskDto(task.getId(), task.getDescription(), task.getStatus(), task.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    /**
     * Создает новую задачу.
     *
     * @param task  задача, переданная в теле запроса
     * @return      созданная задача
     */
    @PostMapping("/tasks")
    public TaskDto createTask(@RequestBody Task task) {

        return taskMapper.map(taskService.save(task));

    }

    /**
     * Возвращает задачу по идентификатору.
     *
     * @param id    идентификатор задачи
     * @return      найденная задача
     */
    @GetMapping("/tasks/{id}")
    public TaskDto getTask(@PathVariable("id") Long id) {

        return taskMapper.map(taskService.findById(id));

    }

    /**
     * Обновляет данные задачи по идентификатору.
     *
     * @param id            идентификатор задачи, которую нужно обновить
     * @param changedTask   задача, данные которой нужно перенести в обновляемую задачу
     * @return              обновленная задача
     */
    @PutMapping("/tasks/{id}")
    public TaskDto updateTask(@PathVariable("id") Long id, @RequestBody Task changedTask) {

        return taskMapper.map(taskService.update(id, changedTask));

    }

    /**
     * Обновляет статус задачи по идентификатору (полное обновление объекта).
     *
     * @param id            идентификатор задачи, статус которой нужно обновить
     * @param changedTask   задача, данные которой нужно перенести в обновляемую задачу
     * @return              задача после обновления статуса
     */
    @PutMapping("/tasks/{id}/status")
    public TaskDto updateStatus(@PathVariable("id") Long id, @RequestBody Task changedTask) {

        return taskMapper.map(taskService.update(id, changedTask.getStatus()));

    }

    /**
     * Обновляет статус задачи, применяя JSON Patch к статусу (частичное обновление объекта).
     *
     * @param id    идентификатор задачи, статус которой нужно обновить
     * @param patch JSON Patch для обновления статуса
     * @return      задача после применения патча
     */
    @PatchMapping(path = "/tasks/{id}/status", consumes = "application/json-patch+json")
    public TaskDto updateStatus(@PathVariable("id") Long id, @RequestBody JsonPatch patch) {

        return taskMapper.map(taskService.update(id, patch));

    }

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id    идентификатор задачи, которую нужно удалить
     */
    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable("id") Long id) {

        taskService.deleteById(id);

    }

}
