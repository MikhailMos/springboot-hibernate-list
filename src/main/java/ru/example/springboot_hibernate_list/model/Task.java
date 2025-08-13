package ru.example.springboot_hibernate_list.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    @NotBlank
    @Size(
            min = 5,
            message = "The message length mast be more 5 characters"
    )
    private String description;

    @Column(name = "status")
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
                ", description='" + description + '\'' +
                ", status=" + status.name() +
                '}';
    }

    public Task copyWithoutId(Task t) {
        this.description = t.getDescription();
        this.status = t.getStatus();

        if (status == null) {
            this.status = TaskStatus.TODO;
        }

        return this;
    }

}
