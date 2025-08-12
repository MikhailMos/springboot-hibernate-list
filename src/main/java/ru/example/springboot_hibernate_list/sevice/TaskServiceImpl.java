package ru.example.springboot_hibernate_list.sevice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.example.springboot_hibernate_list.entity.TaskStatus;
import ru.example.springboot_hibernate_list.model.Task;
import ru.example.springboot_hibernate_list.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Task> readAll() {
        return taskRepository.findAll();
    }

    @Override
    public void create(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task read(Long id) {
        Optional<Task> taskData = taskRepository.findById(id);

        if (taskData.isPresent()) {
            return taskData.get();
        }

        return null;
    }

    @Override
    public Task update(Long id, Task task) throws IllegalArgumentException {
        if (taskRepository.existsById(id)) {

            // Checking correct status, if not correct throw error
            TaskStatus.valueOf(task.getStatus().toUpperCase().replace('-', '_'));

            task.setId(id);
            if (task.getStatus() == null) {
                task.setStatus(TaskStatus.TODO.getView());
            }
            taskRepository.save(task);
            return task;
        }

        return null;
    }

    @Override
    public Task update(Long id, String status) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(status);
            taskRepository.save(task);
            return task;
        }

        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public String getStatusFromJson(String request) throws IllegalArgumentException, Exception {

        try {
            JsonNode root = objectMapper.readTree(request);
            String statusStr = root.hasNonNull("status") ? root.get("status").asText() : null;

            if (statusStr == null) {
                return null;
            }

            TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase().replace('-', '_'));

            return statusStr;
        } catch (IllegalArgumentException ex) {
            // Unrecognized enum value
            throw new IllegalArgumentException(ex);
        } catch (Exception ex) {
            // Handling other errors
            throw new Exception(ex);
        }
    }

}
