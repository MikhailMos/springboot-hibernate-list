package ru.example.springboot.hibernate.list.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Детали токена, связанные с пользователем, включая сам токен и временные метки.
 * Используется для передачи информации о выданном токене между слоями.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {

    /** Идентификатор пользователя. */
    private Long userId;
    /** Токен. */
    private String token;
    /** Дата выпуска токена. */
    private Date issuedAt;
    /** Срок действия токена до этой даты. */
    private  Date expiresAt;

}
