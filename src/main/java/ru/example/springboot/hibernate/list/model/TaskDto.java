package ru.example.springboot.hibernate.list.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность, которую отдаем на фронт-энд вместо Task.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String description;
    private TaskStatus status = TaskStatus.TODO;
    private String owner;
}
