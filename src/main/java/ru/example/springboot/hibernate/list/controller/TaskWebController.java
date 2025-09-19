package ru.example.springboot.hibernate.list.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.model.TaskStatus;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.service.TaskService;
import ru.example.springboot.hibernate.list.service.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для управления задачами через web интерфейс.
 */
@Controller
@RequiredArgsConstructor
public class TaskWebController {

    /**
     * Экземпляр класса содержащий логику обработки данных
     *
     * @see TaskService
     */
    private final TaskService taskService;
    private final UserService userService;

    /**
     * Показывает список всех задач (осн. страница).
     *
     * @return имя шаблона основной страницы
     */
    @GetMapping({"/", "/index"})
    public String indexPage(Model model, Principal principal) {
        List<Task> tasks = taskService.findAllByUserUsername(principal.getName());
        //List<Task> tasks = taskService.findAll();
        // отсортируем по id
        Collections.sort(tasks, Comparator.comparing(Task::getId));
        model.addAttribute("tasks", tasks);
        return "index";
    }

    /**
     * Показывает форму добавления задачи.
     *
     * @param model хранит данные в виде пар "ключ-значение"
     * @return      имя шаблона формы добавления
     */
    @GetMapping("/add")
    public String addTaskPage(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", TaskStatus.values());
        return "task-add";
    }

    /**
     * Сохраняет данные формы в БД.
     *
     * @param model хранит данные в виде пар "ключ-значение"
     * @param task  связанный параметр метода с именем атрибута модели
     * @return перенаправление на шаблон стартовой страницы
     */
    @PostMapping("/add")
    public String addTask(Model model,
                          @ModelAttribute("task") Task task,
                          Principal principal) {

        Optional<UserEntity> optionalUser = userService.getUserByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            new UsernameNotFoundException(String.format("Пользователь '%s' не найден", principal.getName()));
        }

        task.setUser(optionalUser.get());

        Task savedTask = taskService.save(task);
        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String editTaskPage(@PathVariable Long id,
                                Model model) {

        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        return "task-edit";
    }

    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable Long id,
                            Model model,
                           Principal principal,
                           @ModelAttribute("task") Task changedTask) {

        Optional<UserEntity> optionalUser = userService.getUserByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            new UsernameNotFoundException(String.format("Пользователь '%s' не найден", principal.getName()));
        }

        changedTask.setUser(optionalUser.get());

        taskService.update(id, changedTask);
        model.addAttribute("message", "Задача с id = '" + id + "' была изменена.");
        return "redirect:/index";
    }

    /**
     * Удаляет задачу и перенаправляет на стартовую страницу.
     *
     * @param model хранит данные в виде пар "ключ-значение"
     * @return перенаправление на шаблон стартовой страницы
     */
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             Model model) {

        taskService.deleteById(id);
        model.addAttribute("isInfo", true);
        model.addAttribute("messageHeader", "Информация!");
        model.addAttribute("message", "Задача с id = '" + id + "' была успешно удалена.");
        return "redirect:/index";
    }

}
