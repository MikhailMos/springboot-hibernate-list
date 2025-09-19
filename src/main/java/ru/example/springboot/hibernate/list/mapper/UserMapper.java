package ru.example.springboot.hibernate.list.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.example.springboot.hibernate.list.model.UserDto;
import ru.example.springboot.hibernate.list.model.UserEntity;

/**
 * Маппер для преобразования между сущностями пользователя и их DTO представлениями.
 * Используется MapStruct для автоматической генерации реализаций.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует сущность пользователя в DTO.
     *
     * @param userEntity исходная сущность пользователя
     * @return DTO-представление пользователя
     */
    UserDto map(UserEntity userEntity);

    /**
     * Обратное преобразование: DTO в сущность пользователя.
     * Реализация создается на основе аннотации @InheritInverseConfiguration.
     *
     * @param userDto DTO пользователя
     * @return сущность пользователя
     */
    @InheritInverseConfiguration
    UserEntity map(UserDto userDto);
}
