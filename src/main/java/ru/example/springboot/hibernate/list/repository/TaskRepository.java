package ru.example.springboot.hibernate.list.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.springboot.hibernate.list.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
