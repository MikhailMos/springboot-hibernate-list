package ru.example.springboot.hibernate.list.model.exception;

/**
 * Исключение возникающее, когда учетная запись пользователя отключена,
 * либо когда недостаточно прав, аналогично ошибке 403.
 */
public class UnauthorizedException extends ApiException {

    /**
     * Создаёт новое исключение ResourceForbiddenException с указанным подробным сообщением
     * и кодом RESOURCE_FORBIDDEN.
     *
     * @param message подробное сообщение, описывающее почему ресурс запрещен
     */
    public UnauthorizedException(String message) {
        super(message, "RESOURCE_FORBIDDEN");
    }

    /**
     * Создаёт новое исключение ResourceForbiddenException с указанным подробным сообщением.
     *
     * @param message   подробное сообщение, описывающее почему ресурс запрещен
     * @param errorCode код ошибки
     */
    public UnauthorizedException(String message, String errorCode) {
        super(message, errorCode);
    }
}
