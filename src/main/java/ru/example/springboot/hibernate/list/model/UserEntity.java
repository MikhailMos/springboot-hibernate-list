package ru.example.springboot.hibernate.list.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Сущность пользователя. Представляет запись в таблице "users".
 */
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

    /** Конструктор по умолчанию. */
    public UserEntity() {
    }

    /**
     * Создает пользователя.
     *
     * @param username  имя пользователя
     * @param password  пароль пользователя
     * @param role      роль пользователя
     * @param enabled   флаг активности учетной записи
     * @param createdAt дата и время создания учетной записи
     * @param updatedAt дата и время изменения учетной записи
     */
    public UserEntity(String username, String password, UserRole role, boolean enabled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Возвращает идентификатор пользователя.
     *
     * @return идентификатор пользователя
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор пользователя.
     *
     * @param id идентификатор пользователя
     */
    public void setId(Long id) {
        this.id = id;
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
     * @param username имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Возвращает пароль.
     *
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль.
     *
     * @param password пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Возвращает роль пользователя
     *
     * @return роль пользователя
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Устанавливает роль пользователю
     *
     * @param role роль пользователя
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Возвращает флаг активности учетной записи.
     *
     * @return флаг активности
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Устанавливает флаг активности учетной записи.
     *
     * @param enabled флаг активности
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Возвращает дату создания учетной записи.
     *
     * @return дата создания учетной записи
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Устанавливает дату создания учетной записи.
     *
     * @param createdAt дата создания
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Возвращает дату изменения учетной записи.
     *
     * @return дата изменения
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Устанавливает дату изменения учетной записи.
     *
     * @param updatedAt дата изменения
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

