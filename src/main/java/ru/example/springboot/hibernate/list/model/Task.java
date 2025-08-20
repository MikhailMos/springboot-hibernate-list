package ru.example.springboot.hibernate.list.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 *  Сущность задачи. представляет запись в таблице "tasks".
 */
@Entity
@Table(name = "tasks")
public class Task {

    /**
     * Идентификатор задачи. Значение генерируется автоматически.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Описание задачи. Обязательно для заполнения.
     */
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description must not be blank")
    @Size(min = 5, message = "The message length mast be more 5 characters")
    private String description;

    /**
     * Статус задачи. По умолчанию to-do.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    /**
     * Конструктор по умолчанию, обязательный для JPA.
     */
    public Task() {
    }

    /**
     * Создает задачу с описанием. Статус устанавливается по умолчанию.
     *
     * @param description описание задачи
     */
    public Task(String description) {
        this.description = description;
    }

    /**
     * Создает задачу с описанием и статусом.
     *
     * @param description описание задачи
     * @param status      статус задачи
     */
    public Task(String description, TaskStatus status) {
        this.description = description;
        this.status = status == null ? TaskStatus.TODO : status;
    }

    // GETTERS

    /**
     * Возвращает идентификатор задачи.
     *
     * @return идентификатор задачи
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает описание задачи.
     *
     * @return описание задачи
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает статус задачи.
     *
     * @return статус задачи
     */
    public TaskStatus getStatus() {
        return status;
    }

    // SETTERS

    /**
     * Устанавливает идентификатор задачи.
     *
     * @param id идентификатор задачи
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Устанавливает описание задачи.
     *
     * @param description описание задачи
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Устанавливает статус задачи.
     *
     * @param status новый статус задачи
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Возвращает строковое представление объекта задачи.
     *
     * @return строка с информацией о задаче
     */
    @Override
    public String toString() {
        return "Task {" +
                "id=" + id +
                ", description='" + description +
                ", status=" + status.name() +
                "}";
    }

    /**
     * Хэш-код задачи. Основан на полях id, description и status.
     *
     * @return хэш-код задачи
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, status);
    }

    /**
     * Сравнивает две задачи по значению полей id, description и status.
     *
     * @param obj объект для сравнения
     * @return true если объекты эквивалентны по значениям, иначе false
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) { return true; }
        if (!(obj instanceof Task)) { return false; }

        Task otherTask = (Task) obj;

        return Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status);
    }

    /**
     * Возвращает копию задачи без идентификатора.
     * Полезно, когда необходимо создать новый экземпляр на основе существующего,
     * но без привязки к первичному ключу в базе данных.
     *
     * @param t исходная задача
     * @return новая задача с теми же полями, кроме id
     */
    public Task copyWithoutId(Task t) {

        if (t == null) {
            return this;
        }

        this.description = t.getDescription();
        this.status = t.getStatus();

        if (this.status == null) {
            this.status = TaskStatus.TODO;
        }

        return this;
    }

}
