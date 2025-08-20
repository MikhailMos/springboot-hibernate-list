package ru.example.springboot.hibernate.list.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.springboot.hibernate.list.model.exception.ResourceNotFoundException;
import ru.example.springboot.hibernate.list.model.exception.ValidationErrorResponse;
import ru.example.springboot.hibernate.list.model.exception.Violation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений приложения.
 * <p>
 * Этот класс перехватывает исключения, возникающие в слоях контроллеров,
 * и преобразует их в удобочитаемые HTTP-ответы с соответствующими кодами статуса.
 * Добавление конкретных обработчиков упрощает диагностику и обеспечивает
 * единообразие форматов ошибок по всему API.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик исключения ResourceNotFoundException.
     * Возвращает код состояния 404 (Not Found) и тело ошибки типа Violation.
     *
     * @param ex пойманное исключение, сигнализирующее об отсутствии запрашиваемого ресурса
     * @return объект Violation с деталями нарушения
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Violation catchResourceNotFoundException(ResourceNotFoundException ex) {
        return new Violation("", ex.getMessage());
    }

    /**
     * Обработчик исключения ConstraintViolationException.
     * Выбрасывается при проверке валидности на уровне параметров метода.
     * Возвращает код состояния 400 (Bad Request) и тело ошибки типа ValidationErrorResponse.
     *
     * @param ex пойманное исключение валидации ограничений
     * @return объект ValidationErrorResponse с детализированными нарушениями
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse catchConstraintViolationException(ConstraintViolationException ex) {
        final List<Violation> violations = ex
                                            .getConstraintViolations()
                                            .stream()
                                            .map(violation ->  new Violation(
                                                                                        violation.getPropertyPath().toString(),
                                                                                        violation.getMessage()
                                                                                    )
                                            )
                                            .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }

    /**
     * Обработчик исключения MethodArgumentNotValidException.
     * Выбрасывается при проверке валидности на уровне проверки тела запроса.
     * Возвращает код состояния 400 (Bad Request) и тело ошибки типа ValidationErrorResponse.
     *
     * @param ex исключение, возникающее при невалидных аргументах метода
     * @return объект ValidationErrorResponse с деталями ошибок валидации аргументов
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse catchMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        final List<Violation> violations = ex
                                            .getFieldErrors()
                                            .stream()
                                            .map(error ->  new Violation(error.getField(), error.getDefaultMessage()))
                                            .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }

    /**
     * Обработчик исключения HttpMessageNotReadableException (и связанных).
     * Возвращает код состояния 400 (Bad Request) и тело ошибки типа Violation.
     *
     * @param ex исключение, указывающее на проблему с читаемостью HTTP-сообщения
     * @return объект Violation с деталями проблемы
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation catchHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new Violation("", ex.getMessage());
    }

    /**
     * Обработчик исключения JsonPatchException.
     * Выбрасывается если некоторые атрибуты в фактическом JSON отсутствуют.
     * Возвращает код состояния 400 (Bad Request) и тело ошибки типа Violation.
     *
     * @param ex исключение, возникающее при применении JSON Patch
     * @return объект Violation с деталями проблемы
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation catchJsonPatchException(JsonPatchException ex) {
        return new Violation("", ex.getMessage());
    }

    /**
     * Обработчик исключения JsonProcessingException.
     * Выбрасывается если в процессе сериализации/десериализации возникла ошибка
     * Возвращает код состояния 400 (Bad Request) и тело ошибки типа Violation.
     *
     * @param ex исключение, возникающее при обработке JSON
     * @return объект Violation с деталями проблемы
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation catchJsonProcessingException(JsonProcessingException ex) {
        return new Violation("", ex.getMessage());
    }
}
