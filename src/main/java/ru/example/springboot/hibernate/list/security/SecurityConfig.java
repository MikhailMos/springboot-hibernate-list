package ru.example.springboot.hibernate.list.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.example.springboot.hibernate.list.service.UserService;
import ru.example.springboot.hibernate.list.util.JwtUtil;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${root-api-mapping.path}")
    private String rootApiMappingPath;

    /**
     * Предоставляет AuthenticationManager, используемый процессом аутентификации.
     *
     * @param authenticationConfiguration   конфигурация, содержащая настройки аутентификации
     * @return                              настроенный AuthenticationManager
     * @throws Exception                    если произошла ошибка при создании менеджера
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Настраивает HTTP-безопасность для API, включая то, какие конечные точки
     * требуют аутентификации, настройки CSRF, управление сеансами и фильтры.
     *
     * @param http          HttpSecurity для настройки
     * @return              построенный SecurityFilterChain
     * @throws Exception    если произошла ошибка, настройка безопасности
     */
    @Bean
    @Order(1)
    public SecurityFilterChain securityApiFilterChain(HttpSecurity http) throws Exception {

        // Публичные маршруты, не требующие аутентификации.
        final String[] publicRoutes = { rootApiMappingPath + "/auth/login", rootApiMappingPath + "/auth/register"};
        // Маршруты только для пользователя с ролью АДМИН
        final String[] routsAdminOnly = {rootApiMappingPath + "/auth/users/**"};

        return http
                .csrf(csrf -> csrf.disable())
                .securityMatcher(rootApiMappingPath + "/**")
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(publicRoutes).permitAll()
                                .requestMatchers(routsAdminOnly).hasAuthority("ADMIN")//.hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                            sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Настраивает HTTP-безопасность для приложения, включая то, какие конечные точки
     * требуют аутентификации и фильтры.
     *
     * @param http          HttpSecurity для настройки
     * @return              построенный SecurityFilterChain
     * @throws Exception    если произошла ошибка, настройка безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Публичные маршруты, не требующие аутентификации.
        final String[] publicRoutes = {"/login", "/register"};
        // Статические маршруты, не требующие аутентификации.
        final String[] staticRoutes = {"/static/**", "/css/**", "/js/**", "/images/**", "/favicon.ico"};

        return http
                .authorizeHttpRequests(auth ->
                    auth
                            .requestMatchers(HttpMethod.OPTIONS).permitAll()
                            .requestMatchers(publicRoutes).permitAll()
                            .requestMatchers(staticRoutes).permitAll()
                            .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/login") // куда будет направлен запрос для проверки пользователя
                                .defaultSuccessUrl("/index")
                                .failureUrl("/login?error=true")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login")
                                .permitAll()
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Создает и предоставляет компонент JwtRequestFilter, управляемый Spring.
     *
     * @return  экземпляр JwtRequestFilter
     */
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtUtil, userService);
    }

}
