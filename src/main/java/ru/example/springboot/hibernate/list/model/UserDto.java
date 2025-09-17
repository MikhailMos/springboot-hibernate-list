package ru.example.springboot.hibernate.list.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Сущность, которую отдаем на фронт-энд вместо UserEntity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    /**
     * Идентификатор задачи. Значение генерируется автоматически.
     */
    private Long id;

    /** Имя пользователя. Обязательно для заполнения. */
    private String username;

    /** Пароль пользователя. Обязательно для заполнения. */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /** Роль пользователя. Обязательно для заполнения. */
    private UserRole role;

    /** Флаг активации учетной записи. По умолчанию выключена. */
    private boolean enabled = false;

    /** Дата создания учетной записи. */
    private LocalDateTime createdAt;

    /** Дата изменения учетной записи. */
    private LocalDateTime updatedAt;

    /**
     * Вместо пароля подставляет маску из звездочек.
     *
     * @return строка из звездочек
     */
    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }

}
