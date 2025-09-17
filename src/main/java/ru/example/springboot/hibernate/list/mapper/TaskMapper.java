package ru.example.springboot.hibernate.list.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.model.TaskDto;

/**
 * Маппер для преобразования между сущностями задач и их DTO представлениями.
 * Используется MapStruct для автоматической генерации реализаций.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto map(Task task);

    @InheritInverseConfiguration
    Task map(TaskDto taskDto);
}
