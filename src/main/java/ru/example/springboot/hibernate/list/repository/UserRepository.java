package ru.example.springboot.hibernate.list.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.springboot.hibernate.list.model.UserEntity;

import java.util.Optional;

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
     * @return          optional содержащий данные пользователя
     */
    Optional<UserEntity> findByUsername(String username);
}
