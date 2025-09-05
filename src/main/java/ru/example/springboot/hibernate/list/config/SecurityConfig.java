package ru.example.springboot.hibernate.list.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.example.springboot.hibernate.list.service.UserService;
import ru.example.springboot.hibernate.list.util.JwtUtil;

/**
 * Конфигурация Spring Security для приложения.
 * Менеджер аутентификации, цепочку фильтров безопасности
 * и фильтрующий компонент JWT, а также общедоступные маршруты.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /** Публичные маршруты, не требующие аутентификации. */
    private final String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login", "/login", "/login-form", "/register"};
    /** Статические маршруты, не требующие аутентификации. */
    private final String[] staticRoutes = {"/static/**", "/css/**", "/js/**", "/images/**", "/favicon.ico"};

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(@Autowired UserService userService, @Autowired JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

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
     * Настраивает HTTP-безопасность для приложения, включая то, какие конечные точки
     * требуют аутентификации, настройки CSRF, управление сеансами и фильтры.
     *
     * @param http          HttpSecurity для настройки
     * @return              построенный SecurityFilterChain
     * @throws Exception    если произошла ошибка, настройка безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests) ->
                    authorizeHttpRequests
                            .requestMatchers(HttpMethod.OPTIONS).permitAll()
                            .requestMatchers(staticRoutes).permitAll()
                            .requestMatchers(publicRoutes).permitAll()
                            .anyRequest().authenticated()
                )
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/login-form")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/index", true)
                                .failureUrl("/login-form?error=true")
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login-form?logout")
                        .permitAll()
                )
                .sessionManagement((sessionManagement) ->
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                            .sessionConcurrency((sessionConcurrtycy) ->
//                                    sessionConcurrtycy
//                                            .maximumSessions(1)
//                                            //.expiredUrl("/login?expired")
//                            )
                );

        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
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

//    //** Адаптер для аутентификации через Dao */
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//    /**
//     * Устанавливает тип кодировщика.
//     *
//     * @return кодировщик
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
