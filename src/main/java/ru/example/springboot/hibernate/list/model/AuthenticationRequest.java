package ru.example.springboot.hibernate.list.model;

/**
 * Контейнер данных для входа пользователя в систему.
 * Содержит имя пользователя и пароль.
 */
public class AuthenticationRequest {

    /** Имя пользователя, используемое для входа. */
    private String username;

    /** Пароль пользователя. */
    private String password;

    /** Конструктор по умолчанию. */
    public AuthenticationRequest() {
    }

    /**
     * Конструктор с параметрами.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username новое имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password новый пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
