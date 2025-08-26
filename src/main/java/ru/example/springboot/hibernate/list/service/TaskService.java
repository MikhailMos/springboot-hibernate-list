package ru.example.springboot.hibernate.list.service;

import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.model.TaskStatus;

import java.util.List;

/**
 * Интерфейс содержащий минимальный набор методов, необходимых для реализации
 * логики приложения.
 * <p>Задача {@code Task} проверяется на валидацию.</p>
 */
@Validated
public interface TaskService {

    /**
     * Метод возвращает список всех задач.
     *
     * @return список всех задач
     */
    List<Task> findAll();

    /**
     * Метод сохраняет задачу и возвращает сохраненную задачу.
     *
     * @param task  задача, которую нужно сохранить
     * @return      сохраненная задача
     * @see Task
     */
    Task save(@Valid Task task);

    /**
     * Метод возвращает задачу найденную в БД по {@code id}.
     * <p>!!! Необходимо предусмотреть случай, когда id нет в БД !!!</p>
     *
     * @param id    числовой идентификатор задачи, которую нужно найти
     * @return      найденная задача
     * @see Task
     */
    Task findById(Long id);

    /**
     * Метод полностью обновляет задачу.
     *
     * @param id          числовой идентификатор задачи, которую нужно изменить
     * @param changedTask задача содержащая измененные данные
     * @return            измененная задача
     * @see Task
     */
    Task update(Long id, @Valid Task changedTask);

    /**
     * Метод обновляет статус задачи. Задача перезаписывается полностью.
     *
     * @param id        числовой идентификатор задачи, статус которой нужно изменить
     * @param newStatus новый статус
     * @return          измененная задача
     * @see Task
     */
    Task update(Long id, TaskStatus newStatus);

    /**
     * Метод обновляет статус задачи. Обновляется только часть задачи.
     *
     * @param id    числовой идентификатор задачи, статус которой нужно изменить
     * @param patch список операций, которые нужно последовательно применить к целевому объекту
     * @return      измененная задача
     * @see Task
     */
    Task update(Long id, JsonPatch patch);

    /**
     * Метод удаляет задачу из БД по {@code id}.
     *
     * @param id    числовой идентификатор задачи, которую нужно удалить
     */
    void deleteById(Long id);

}
