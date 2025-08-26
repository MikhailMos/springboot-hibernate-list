package ru.example.springboot.hibernate.list.model.exception;

/**
 * Исключение возникающее, когда неверный пароль или учетная запись отключена.
 * Это исключение среды выполнения, которое можно использовать для обозначения
 *  состояния, аналогично ошибке 401, на уровне сервиса или контроллера.
 */
public class UnauthorizedException extends RuntimeException {

    /** Имя поля. */
    private String fieldName;

    /**
     * Создает новое исключение UnauthorizedException с указанным подробным сообщением.
     *
     * @param message подробное сообщение, описывающее возникновение исключения
     * @param fieldName имя поля, если поле не известно передавай пустую строку
     */
    public UnauthorizedException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    /**
     * Возвращает имя поля
     *
     * @return имя поля
     */
    public String getFieldName() {
        return fieldName;
    }
}
