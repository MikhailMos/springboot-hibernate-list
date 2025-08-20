package ru.example.springboot.hibernate.list.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.springboot.hibernate.list.model.Task;

/**
 * Интерфейс расширяемый {@code JpaRepository<T, ID>},
 * предоставляет готовые методы для запросов к базе данных.
 * @see JpaRepository
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

}
