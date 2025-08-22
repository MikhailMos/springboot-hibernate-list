package ru.example.springboot.hibernate.list.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.service.TaskService;

import java.util.List;

/**
 * REST контроллер для управления задачами.
 * Обрабатывает HTTP-запросы, связанные с созданием, чтением, обновлением и удалением задач.
 */
@RestController
@RequestMapping("/${root-mapping.path}")
public class TaskController {

    /**
     * Экземпляр класса содержащий логику обработки данных
     *
     * @see TaskService
     */
    private final TaskService taskService;

    /**
     * Создает экземпляр TaskController с внедрением сервиса задач.
     *
     * @param taskService сервис для выполнения бизнес-логики по задачам
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Возвращает список всех задач.
     *
     * @return список объектов Task
     */
    @GetMapping("/tasks")
    public List<Task> getAllTasks() {

        return taskService.findAll();

    }

    /**
     * Создает новую задачу.
     *
     * @param task  задача, переданная в теле запроса
     * @return      созданная задача
     */
    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {

        return taskService.save(task);

    }

    /**
     * Возвращает задачу по идентификатору.
     *
     * @param id    идентификатор задачи
     * @return      найденная задача
     */
    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable("id") Long id) {

        return taskService.findById(id);

    }

    /**
     * Обновляет данные задачи по идентификатору.
     *
     * @param id            идентификатор задачи, которую нужно обновить
     * @param changedTask   задача, данные которой нужно перенести в обновляемую задачу
     * @return              обновленная задача
     */
    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable("id") Long id, @RequestBody Task changedTask) {

        return taskService.update(id, changedTask);

    }

    /**
     * Обновляет статус задачи по идентификатору (полное обновление объекта).
     *
     * @param id            идентификатор задачи, статус которой нужно обновить
     * @param changedTask   задача, данные которой нужно перенести в обновляемую задачу
     * @return              задача после обновления статуса
     */
    @PutMapping("/tasks/{id}/status")
    public Task updateStatus(@PathVariable("id") Long id, @RequestBody Task changedTask) {

        return taskService.update(id, changedTask.getStatus());

    }

    /**
     * Обновляет статус задачи, применяя JSON Patch к статусу (частичное обновление объекта).
     *
     * @param id    идентификатор задачи, статус которой нужно обновить
     * @param patch JSON Patch для обновления статуса
     * @return      задача после применения патча
     */
    @PatchMapping(path = "/tasks/{id}/status", consumes = "application/json-patch+json")
    public Task updateStatus(@PathVariable("id") Long id, @RequestBody JsonPatch patch) {

        return taskService.update(id, patch);

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
