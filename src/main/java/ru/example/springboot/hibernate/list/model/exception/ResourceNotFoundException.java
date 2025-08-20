package ru.example.springboot.hibernate.list.model.exception;

/**
 * Исключение возникающее, когда запрошенный ресурс не может быть найден.
 * Это исключение среды выполнения, которое можно использовать для обозначения
 *  состояния, аналогично ошибке 404, на уровне сервиса или контроллера.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Создаёт новое исключение ResourceNotFoundException с указанным подробным сообщением.
     *
     * @param message подробное сообщение, описывающее ресурс, который не был найден
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
