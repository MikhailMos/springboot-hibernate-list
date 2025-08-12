package ru.example.springboot_hibernate_list.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.springboot_hibernate_list.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
