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

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Violation catchResourceNotFoundException(ResourceNotFoundException ex) {
        return new Violation("", ex.getMessage());
    }

    /**
     * ConstraintViolationException - выбрасывается при проверке валидности на уровне параметров метода.
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
     * MethodArgumentNotValidException - выбрасывается при проверке валидности на уровне проверки тела запроса.
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation catchHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new Violation("", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation catchJsonPatchException(JsonPatchException ex) {
        return new Violation("", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation catchJsonProcessingException(JsonProcessingException ex) {
        return new Violation("", ex.getMessage());
    }
}
