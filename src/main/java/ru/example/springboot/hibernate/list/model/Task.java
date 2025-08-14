package ru.example.springboot.hibernate.list.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description must not be blank")
    @Size(min = 5, message = "The message length mast be more 5 characters")
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    public Task() {
    }

    public Task(String description) {
        this.description = description;
    }

    public Task(String description, TaskStatus status) {
        this.description = description;
        this.status = status == null ? TaskStatus.TODO : status;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task {" +
                "id=" + id +
                ", description='" + description +
                ", status=" + status.name() +
                "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, status);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) { return true; }
        if (!(obj instanceof Task)) { return false; }

        Task otherTask = (Task) obj;

        return Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status);
    }

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
