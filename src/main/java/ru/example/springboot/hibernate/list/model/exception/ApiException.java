package ru.example.springboot.hibernate.list.model.exception;

import lombok.Getter;

/**
 * Единый класс для ошибок в приложении.
 */
public class ApiException extends RuntimeException {

    @Getter
    protected String errorCode;

    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
