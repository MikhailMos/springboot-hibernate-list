package ru.example.springboot.hibernate.list.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

/**
 * Ответ аутентификации, содержащий идентификатор пользователя, токен и временные метки.
 * Этот класс используется для передачи информации о результате аутентификации между слоями.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthenticationResponse {

    private Long userId;
    private String token;
    private Date issuedAt;
    private Date expiredAt;

    /** Конструктор по умолчанию. */
    public AuthenticationResponse() {
    }

    /**
     * Конструктор с параметрами.
     *
     * @param userId    идентификатор пользователя
     * @param token     токен аутентификации
     * @param issuedAt  дата и время выдачи токена
     * @param expiredAt дата и время истечения срока действия токена
     */
    public AuthenticationResponse(Long userId, String token, Date issuedAt, Date expiredAt) {
        this.userId = userId;
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
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
     * Возвращает токен аутентификации.
     *
     * @return токен аутентификации
     */
    public String getToken() {
        return token;
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
     * Возвращает дату и время истечения срока действия токена.
     *
     * @return дата и время истечения срока действия токена
     */
    public Date getExpiredAt() {
        return expiredAt;
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
     * Устанавливает токен аутентификации.
     *
     * @param token токен аутентификации
     */
    public void setToken(String token) {
        this.token = token;
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
     * Устанавливает дату и время истечения срока действия токена.
     *
     * @param expiresAt дата и время истечения срока действия токена
     */
    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }
}
