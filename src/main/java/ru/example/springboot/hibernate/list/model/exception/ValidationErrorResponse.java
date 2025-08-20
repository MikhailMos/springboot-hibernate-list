package ru.example.springboot.hibernate.list.model.exception;

import java.util.List;

/**
 * Представляет собой ответ, содержащий список нарушений проверки.
 * Этот класс обычно используется для передачи подробной информации о
 * нескольких ошибках проверки полей в одном ответе.
 */
public class ValidationErrorResponse {
    /**
     * Cписок нарушений, который необходимо включить в ответ.
     */
    private final List<Violation> violations;

    /**
     * Создает ValidationErrorResponse с предоставленным списком нарушений.
     *
     * @param violations список нарушений, который необходимо включить в ответ.
     */
    public ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }

    /**
     * Возвращает список нарушений, содержащихся в этом ответе.
     *
     * @return список нарушений
     */
    public List<Violation> getViolations() {
        return violations;
    }
}
