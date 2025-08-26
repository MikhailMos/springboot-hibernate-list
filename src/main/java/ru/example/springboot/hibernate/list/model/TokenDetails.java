package ru.example.springboot.hibernate.list.model;

import java.util.Date;

/**
 * Детали токена, связанные с пользователем, включая сам токен и временные метки.
 * Используется для передачи информации о выданном токене между слоями.
 */
public class TokenDetails {

    /** Идентификатор пользователя. */
    private Long userId;

    /** Токен. */
    private String token;

    /** Дата выпуска токена. */
    private Date issuedAt;

    /** Срок действия токена до этой даты. */
    private  Date expiresAt;

    /** Конструктор по умолчанию. */
    public TokenDetails() {
    }

    /**
     * Конструктор с параметрами.
     *
     * @param userId    идентификатор пользователя
     * @param token     токен аутентификации
     * @param issuedAt  дата и время выдачи токена
     * @param expiresAt дата и время истечения срока действия токена
     */
    public TokenDetails(Long userId, String token, Date issuedAt, Date expiresAt) {
        this.userId = userId;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    /**
     * Возвращает идентификатор пользователя.
     *
     * @return идентификатор пользователя
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Устанавливает идентификатор пользователя.
     *
     * @param userId идентификатор пользователя
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Возвращает токен аутентификации.
     *
     * @return токен аутентификации
     */
    public String getToken() {
        return token;
    }

    /**
     * Устанавливает токен аутентификации.
     *
     * @param token токен аутентификации
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Возвращает дату и время выдачи токена.
     *
     * @return дата и время выдачи токена
     */
    public Date getIssuedAt() {
        return issuedAt;
    }

    /**
     * Устанавливает дату и время выдачи токена.
     *
     * @param issuedAt дата и время выдачи токена
     */
    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    /**
     * Возвращает дату и время истечения срока действия токена.
     *
     * @return дата и время истечения срока действия токена
     */
    public Date getExpiresAt() {
        return expiresAt;
    }

    /**
     * Устанавливает дату и время истечения срока действия токена.
     *
     * @param expiresAt дата и время истечения срока действия токена
     */
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
