package ru.example.springboot.hibernate.list.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.example.springboot.hibernate.list.model.TokenDetails;
import ru.example.springboot.hibernate.list.model.UserDto;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.model.exception.UnauthorizedException;
import ru.example.springboot.hibernate.list.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthWebController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticateTheUser(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {

        try {
            TokenDetails tokenDetails = userService.authenticate(username, password);
            model.addAttribute("Token", tokenDetails.getToken()); // Token
        } catch (UnauthorizedException e) {
            model.addAttribute("messageHeader", "Ошибка!");
            model.addAttribute("message", e.getMessage());
            return "login";
        }

        return "index";

    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        UserDto userDto = new UserDto();
        model.addAttribute("userForm", userDto);

        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  Model model) {

        if (username == null || username.isBlank()) {
            model.addAttribute("messageHeader", "Ошибка!");
            model.addAttribute("message", "Имя пользователя не указано");
            return "register";
        }

        if (password == null || !password.equals(confirmPassword)) {
            model.addAttribute("messageHeader", "Ошибка!");
            model.addAttribute("message", "Пароли не совпадают");
            return "register";
        }

        UserEntity userEntity = new UserEntity(username, password);
        try {
            userService.registerUser(userEntity);
            return "redirect:/login";   //"redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("messageHeader", "Ошибка!");
            model.addAttribute("message", e.getMessage()); // пользователь уже существует
        }

        return "register";
    }

}
