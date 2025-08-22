package ru.example.springboot.hibernate.list.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.springboot.hibernate.list.model.UserEntity;

/**
 * Интерфейс расширяемый {@code JpaRepository<T, ID>},
 * предоставляет готовые методы для запросов к базе данных.
 * @see JpaRepository
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Возвращает объект User по имени.
     *
     * @param username  имя пользователя
     * @return          объект Пользователь
     */
    UserEntity findByUsername(String username);
}
