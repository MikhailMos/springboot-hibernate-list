package ru.example.springboot.hibernate.list.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.model.TaskStatus;

import java.util.List;

@Validated
public interface TaskService {

    List<Task> findAll();
    Task save(@Valid Task task);
    Task findById(Long id);
    Task update(Long id, @Valid Task changedTask);
    Task update(Long id, TaskStatus newStatus);
    Task update(Long id, JsonPatch patch);
    void deleteById(Long id);

}
