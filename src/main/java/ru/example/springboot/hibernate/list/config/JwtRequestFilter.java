package ru.example.springboot.hibernate.list.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.service.UserService;
import ru.example.springboot.hibernate.list.util.JwtUtil;

import java.io.IOException;
import java.util.List;

/**
 * Фильтр, отвечающий за проверку JWT во входящих HTTP-запросах.
 * Если найден действительный токен, соответствующая аутентификация пользователя
 * устанавливается в SecurityContext.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    /**
     * Создает JwtRequestFilter с необходимыми зависимостями.
     *
     * @param jwtUtil утилита для работы с токенами JWT
     * @param userService сервис для загрузки данных пользователя
     */
    public JwtRequestFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Обрабатывает запрос до того, как он достигнет цепочки отправки.
     * Извлекает токен JWT из заголовка авторизации, проверяет его и
     * устанавливает аутентификацию в контексте безопасности, если она действительна.
     *
     * @param request           входящий HTTP-запрос
     * @param response          HTTP-ответ
     * @param filterChain       цепочка фильтров для передачи управления следующему фильтру
     * @throws ServletException если возникает ошибка, специфичная для сервлета
     * @throws IOException      если во время обработки произошла ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        Long userId = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            token = authorizationHeader.substring(BEARER_PREFIX.length());
            userId = jwtUtil.extractUserId(token);
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userEntity = this.userService.getUserById(userId);

            if (jwtUtil.validateToken(token, userEntity)) {
                Claims claims = jwtUtil.extractAllClaims(token);
                String role = claims.get("role", String.class);
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userEntity, null, authorities);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
