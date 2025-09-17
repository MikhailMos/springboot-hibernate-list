package ru.example.springboot.hibernate.list.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность пользователя. Представляет запись в таблице "users".
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    /** Идентификатор пользователя. Значение генерируется автоматически. */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя пользователя. Уникально и обязательно для заполнения. */
    @Column(name = "username", nullable = false, unique = true, length = 64)
    private String username;

    /** Пароль пользователя. Обязательно для заполнения. */
    @Column(name = "password", nullable = false, length = 2048)
    private String password;

    /** Роль пользователя. Обязательно для заполнения. */
    @Column(name = "role", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /** Флаг активации учетной записи. По умолчанию выключена. */
    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    /** Дата создания учетной записи. Не участвует при обновлении! */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Дата изменения учетной записи. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Задачи связанные с пользователем */
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Task> tasks;

    /**
     * Конструктор с минимальным кол-вом параметров.
     * Создает пользователя с ролью USER, включенной записью и с текущим временем создания и изменения.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = UserRole.USER;
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.tasks = new ArrayList<>(10);
    }

}

