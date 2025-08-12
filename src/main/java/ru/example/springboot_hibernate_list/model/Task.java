package ru.example.springboot_hibernate_list.model;

import jakarta.persistence.*;
import ru.example.springboot_hibernate_list.entity.TaskStatus;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status = TaskStatus.TODO.getView();

    public Task() {
    }

    public Task(String description) {
        this.description = description;
    }

    public Task(String description, String status) {
        this.description = description;
        this.status = status == null ? TaskStatus.TODO.getView() : status;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task {" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
