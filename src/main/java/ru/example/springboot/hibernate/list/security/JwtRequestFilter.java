package ru.example.springboot.hibernate.list.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.service.UserService;
import ru.example.springboot.hibernate.list.util.JwtUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Фильтр, отвечающий за проверку JWT во входящих HTTP-запросах.
 * Если найден действительный токен, соответствующая аутентификация пользователя
 * устанавливается в SecurityContext.
 */
@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private JwtUtil jwtUtil;
    private UserService userService;

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

        String username = null;
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            token = authorizationHeader.substring(BEARER_PREFIX.length());
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<UserEntity> optionalUser = this.userService.getUserByUsername(username);
            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username));
            }

            UserEntity userEntity = optionalUser.get();

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
