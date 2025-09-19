package ru.example.springboot.hibernate.list.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.example.springboot.hibernate.list.model.Task;
import ru.example.springboot.hibernate.list.model.TaskStatus;
import ru.example.springboot.hibernate.list.model.exception.ResourceNotFoundException;
import ru.example.springboot.hibernate.list.model.exception.UnauthorizedException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений приложения.
 * Для REST отдает JSON, для Web отдает html.
 * <p>
 * Этот класс перехватывает исключения, возникающие в слоях контроллеров,
 * и преобразует их в удобочитаемые HTTP-ответы с соответствующими кодами статуса.
 * Добавление конкретных обработчиков упрощает диагностику и обеспечивает
 * единообразие форматов ошибок по всему API.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик исключения ResourceNotFoundException.
     * Возвращает код состояния 404 (Not Found) и тело ошибки.
     *
     * @param ex        пойманное исключение, сигнализирующее об отсутствии запрашиваемого ресурса
     * @param request   запрос
     * @return          объект Object с деталями нарушения
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage());
        }

        // вернуть http
        String viewName = getViewName(request.getRequestURI()); // RequestPath previousRequestPath = (RequestPath)request.getAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE);
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("messageHeader", "Ошибка!");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    /**
     * Обработчик исключения ConstraintViolationException.
     * Выбрасывается при проверке валидности на уровне параметров метода.
     * Возвращает код состояния 400 (Bad Request) и тело ошибки типа Object.
     *
     * @param ex        пойманное исключение валидации ограничений
     * @param request   запрос
     * @return          объект Object с детализированными нарушениями
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        final String message = ex
                .getConstraintViolations()
                .stream()
                .map(error ->  error.getPropertyPath() + ": " + error.getMessage())
                .collect(Collectors.joining("\n"));

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message);
        }

        List<Task> list = ex.getConstraintViolations().stream()
                .map(violation -> (Task) violation.getLeafBean())
                .collect(Collectors.toList());

        // вернуть http
        String viewName = getViewName(request.getRequestURI());
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("messageHeader", "Ошибка!");
        mav.addObject("message", message);
        mav.addObject("statuses", TaskStatus.values());
        if (!list.isEmpty()) {
            Task task = list.getFirst();
            task.setUser(null); // иначе вылетает ошибка преобразования String to Long
            mav.addObject("task", task);
        }

        return mav;
    }

    /**
     * Обработчик исключения MethodArgumentNotValidException.
     * Выбрасывается при проверке валидности на уровне проверки тела запроса.
     * Возвращает код состояния 400 (Bad Request) и тело ошибки типа Object.
     *
     * @param ex исключение, возникающее при невалидных аргументах метода
     * @param request запрос
     * @return объект Object с деталями ошибок валидации аргументов
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        final String message = ex
                .getFieldErrors()
                .stream()
                .map(error ->  error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("\n"));

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message);
        }

        // вернуть http
        String viewName = getViewName(request.getRequestURI());
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("messageHeader", "Ошибка!");
        mav.addObject("message", message);
        mav.addObject("statuses", TaskStatus.values());

        return mav;
    }

    /**
     * Обработчик исключения HttpMessageNotReadableException (и связанных).
     * Возвращает код состояния 400 (Bad Request) и тело ошибки.
     *
     * @param ex исключение, указывающее на проблему с читаемостью HTTP-сообщения
     * @param request запрос
     * @return объект ApiException с деталями проблемы
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
        }

        // вернуть http
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());

        return mav;
    }

    /**
     * Обработчик исключения JsonPatchException.
     * Выбрасывается если некоторые атрибуты в фактическом JSON отсутствуют.
     * Возвращает код состояния 400 (Bad Request) и тело ошибки.
     *
     * @param ex исключение, возникающее при применении JSON Patch
     * @param request запрос
     * @return объект Object с деталями проблемы
     */
    @ExceptionHandler(JsonPatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleJsonPatchException(JsonPatchException ex, HttpServletRequest request) {

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
        }

        // вернуть http
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());

        return mav;
    }

    /**
     * Обработчик исключения JsonProcessingException.
     * Выбрасывается если в процессе сериализации/десериализации возникла ошибка
     * Возвращает код состояния 400 (Bad Request) и тело ошибки.
     *
     * @param ex исключение, возникающее при обработке JSON
     * @param request запрос
     * @return объект Object с деталями проблемы
     */
    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleJsonProcessingException(JsonProcessingException ex, HttpServletRequest request) {

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
        }

        // вернуть http
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());

        return mav;
    }

    /**
     * Обработчик исключения SQLException.
     * Выбрасывается если возникает ошибка доступа к базе данных или каких то других ошибках, связанных с базой данных.
     *
     * @param ex        исключение, возникающее в базе данных
     * @param request   запрос
     * @return          объект Object с деталями проблемы
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleSQLException(SQLException ex, HttpServletRequest request) {

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ex.getMessage());
        }

        // вернуть http
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());

        return mav;
    }

    /**
     * Обработчик исключения UnauthorizedException.
     * Выбрасывается если не авторизованный пользователь пытается получить
     * доступ к защищенному ресурсу.
     * Возвращает код состояния 403 (Forbidden) и тело ошибки.
     *
     * @param ex        исключение, возникающее в базе данных
     * @param request   запрос
     * @return          объект Object с деталями проблемы
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Object handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {

        // вернуть JSON
        if (isApiRequest(request)) {
            return getResponseEntityForJson(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage());
        }

        // вернуть http
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", ex.getMessage());

        return mav;
    }

    /**
     * Проверяет какой формат нужно будет отдавать JSON или http.
     *
     * @param request   полученный запрос
     * @return          true - если запрос нужно отдать в формате JSON, иначе false
     */
    private boolean isApiRequest(final HttpServletRequest request) {
        // Определяем, какой формат требуется клиент: JSON (REST) или HTML (Web)
        String contentType = request.getContentType(); // request.getHeader("Content-type");
        return contentType != null && contentType.contains("json");
               // (contentType.contains(MediaType.APPLICATION_JSON_VALUE) || contentType.contains("application/vnd.api+json"));
    }

    /**
     * Возвращает имя формы в зависимости от пути запроса.
     *
     * @param requestURL    пусть запроса
     * @return              имя формы
     */
    private String getViewName(String requestURL) {
        String result = "";

        if (requestURL.endsWith("/add")) {
            result = "task-add";
        } else if (requestURL.contains("/edit/")) {
            result = "task-edit";
        } else if (requestURL.endsWith("/index")) {
            result = "index";
        } else if (requestURL.endsWith("/login")) {
            result = "login";
        } else if (requestURL.endsWith("/register")) {
            result = "register";
        }

        return result;
    }

    /**
     * Подготавливает ResponseEntity и возвращает его.
     *
     * @param httpStatus    перечисление, статус
     * @param errorCode     строковое описание кода
     * @param message       описание ошибки
     * @return              заполненная сущность ответа
     */
    private ResponseEntity<Map<String, String>> getResponseEntityForJson(HttpStatus httpStatus, String errorCode, String message) {

        Map<String, String> body = new HashMap<>();
        body.put("errorCode", errorCode);
        body.put("message", message);

        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

}
