package ru.example.springboot.hibernate.list.model.exception;

/**
 *  Представляет собой единичную ошибку (нарушение) проверки.
 *  <p>
 *      Каждое нарушение содержит понятное пользователю сообщение с описанием
 *      нарушения и название поля, которое не прошло проверку.
 *      Название поля может быть пустым, если ошибка не связана с валидацией.
 *  </p>
 */
public class Violation {
    /**
     * Имя поля, проверка которого потерпела неудачу.
     */
    private final String fieldName;
    /**
     * Сообщение об ошибке.
     */
    private final String message;

    /**
     * Создает ошибку с указанным именем поля и сообщением об ошибке.
     *
     * @param fieldName имя поля, которое не прошло проверку
     * @param message   сообщение об ошибке с описанием нарушения
     */
    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    /**
     * Возвращает имя поля, которое не прошло проверку.
     *
     * @return имя поля
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Возвращает сообщение об ошибке.
     * @return сообщение об ошибке
     */
    public String getMessage() {
        return message;
    }
}
