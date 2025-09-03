package ru.example.springboot.hibernate.list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.springboot.hibernate.list.mapper.UserMapper;
import ru.example.springboot.hibernate.list.model.TokenDetails;
import ru.example.springboot.hibernate.list.model.UserDto;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.model.exception.UnauthorizedException;
import ru.example.springboot.hibernate.list.service.UserService;

@Controller
public class WebAuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    public WebAuthController(@Autowired UserService userService, @Autowired UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "login-form";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        try {
            TokenDetails tokenDetails = userService.authenticate(username, password);
            model.addAttribute("Authorization", tokenDetails.getToken()); // Token
        } catch (UnauthorizedException e) {
            model.addAttribute("error", e.getMessage());
            return "login-form";
        }

        return "index";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {

        UserDto userDto = new UserDto();
        model.addAttribute("userForm", userDto);

        return "register-form";
    }

    @PostMapping("/register")
    public String registerForm(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        // простая валидация и вызов сервиса
        if (username == null || username.isBlank()) {
            model.addAttribute("error", "Имя пользователя не указано");
            return "register-form";
        }

        if (password == null || !password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register-form";
        }

        UserEntity userEntity = new UserEntity(username, password);
        try {
            UserDto userDtoRes = userMapper.map(userService.registerUser(userEntity));
            return "redirect:/login?registered"; //"redirect:/login-form?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()); // пользователь уже существует
        }

        return "register-form";
    }

}
