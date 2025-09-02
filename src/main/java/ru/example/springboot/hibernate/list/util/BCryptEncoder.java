package ru.example.springboot.hibernate.list.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Утилита работы с кодировщиком паролей.
 * Определяет кодирование паролей.
 */
@Component
public class BCryptEncoder {
    /** Кодировщик паролей, используемый для хеширования паролей. */
    private final PasswordEncoder passwordEncoder;

    public BCryptEncoder() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public Boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
