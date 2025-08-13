package ru.example.springboot_hibernate_list.model.exception;

import java.util.List;

public class ValidationErrorResponse {

    private final List<Violation> violations;

    public ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
