package ru.example.springboot.hibernate.list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot.hibernate.list.mapper.UserMapper;
import ru.example.springboot.hibernate.list.model.*;
import ru.example.springboot.hibernate.list.service.UserService;

import java.security.Principal;

/**
 * REST контроллер для обработки аутентификации и регистрации пользователей.
 * Обеспечивает конечные точки по регистрации, входу в систему и получению информации
 * о текущем пользователе.
 */
@RestController
@RequestMapping("/${root-mapping.path}/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

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
     * Аутентификация пользователя.
     *
     * @param authenticationRequest данные для входа (имя пользователя и пароль)
     * @return объект с информацией об аутентификации (например, токен)
     * @throws Exception если произошла ошибка аутентификации
     */
    @PostMapping("/login")
    public AuthenticationResponse loginUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        TokenDetails tokenDetails = userService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        return new AuthenticationResponse(tokenDetails.getUserId(), tokenDetails.getToken(), tokenDetails.getIssuedAt(), tokenDetails.getExpiresAt());
    }

    /**
     * Получение информации о текущем пользователе.
     *
     * @param authentication данные аутентификации текущего пользователя
     * @return DTO с информацией о пользователе
     */
    @GetMapping("/info")
    public UserDto getUserInfo(@RequestBody Authentication authentication) {
        Principal principal = (Principal) authentication.getPrincipal();

        return userMapper.map(userService.getUserByUsername(principal.getName()));
    }

}
