package ru.example.springboot.hibernate.list.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot.hibernate.list.mapper.UserMapper;
import ru.example.springboot.hibernate.list.model.*;
import ru.example.springboot.hibernate.list.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST контроллер для обработки аутентификации и регистрации пользователей.
 * Обеспечивает конечные точки по регистрации, входу в систему и получению информации
 * о текущем пользователе.
 */
@RestController
@RequestMapping("/${root-api-mapping.path}/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Получение информации о текущем пользователе.
     *
     * @param authentication данные аутентификации текущего пользователя
     * @return DTO с информацией о пользователе
     */
    @GetMapping("/info")
    public UserDto getUserInfo(Authentication authentication) {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Optional<UserEntity> optionalUser = userService.getUserByUsername(userEntity.getUsername());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", userEntity.getUsername()));
        }

        return userMapper.map(optionalUser.get());
    }

    /**
     * Аутентификация пользователя.
     *
     * @param userDto данные для входа (имя пользователя и пароль)
     * @return объект с информацией об аутентификации (например, токен)
     * @throws Exception если произошла ошибка аутентификации
     */
    @PostMapping("/login")
    public TokenDetails loginUser(@RequestBody UserDto userDto) throws Exception {

        return userService.authenticate(userDto.getUsername(), userDto.getPassword());
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userDto объект данных пользователя для регистрации
     * @return сериализованный объект DTO зарегистрированного пользователя
     */
    @PostMapping("/register")
    public UserDto registerUser(@RequestBody UserDto userDto) {

        UserEntity userEntity = userMapper.map(userDto);
        return userMapper.map(userService.registerUser(userEntity));
    }

    /**
     * Получение данных о всех пользователях БД.
     *
     * @return список пользователей
     */
    @GetMapping("/users")
    public List<UserDto> getAllUsersFromDataBase() {
        return userService.findAllUsers().stream()
                .map(userEntity ->
                        new UserDto(userEntity))
                .collect(Collectors.toList());
    }

    /**
     * Удаление пользователя.
     *
     * @param id объект данных пользователя для удаления
     * @return структура состоящая из имени пользователя и сообщения
     */
    @DeleteMapping("/users/{id}")
    public Map<String, String> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        Map<String, String> results = new HashMap<>(2);
        results.put("userId", id.toString());
        results.put("message", "Пользователь удален");

        return results;
    }

}
