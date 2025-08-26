package ru.example.springboot.hibernate.list.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.springboot.hibernate.list.model.TokenDetails;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.model.UserRole;
import ru.example.springboot.hibernate.list.model.exception.UnauthorizedException;
import ru.example.springboot.hibernate.list.repository.UserRepository;
import ru.example.springboot.hibernate.list.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Сервис пользователей. Обеспечивает операции по получению и регистрации пользователей,
 * а также аутентификацию и формирование токена.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Возвращает пользователя по имени пользователя.
     *
     * @param username Имя пользователя
     * @return объект UserEntity, соответствующий указанному имени пользователя
     */
    public UserEntity getUserByUsername(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);

        if (userEntityOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return userEntityOptional.get();
    }

    /**
     * Возвращает пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return объект UserEntity, соответствующий указанному идентификатору
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public UserEntity getUserById(Long userId) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if (userEntityOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return userEntityOptional.get();
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param userEntity объект пользователя, который требуется зарегистрировать
     * @return зарегистрированный объект UserEntity
     */
    public UserEntity registerUser(UserEntity userEntity) {

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(userEntity.getCreatedAt());
        userEntity.setEnabled(true);
        if (userEntity.getRole() == null) {
            userEntity.setRole(UserRole.USER);
        }

        return userRepository.save(userEntity);
    }

    /**
     * Аутентифицирует пользователя и возвращает данные токена.
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     * @return объект TokenDetails, содержащий детали аутентификации и токен
     */
    public TokenDetails authenticate(String username, String password) {

        UserEntity userEntity = getUserByUsername(username);

        if (!userEntity.isEnabled()) {
            throw new UnauthorizedException("Account disabled", "username");
        }

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new UnauthorizedException("Invalid password", "password");
        }

        String token = jwtUtil.generateToken(userEntity);

        return new TokenDetails(
                userEntity.getId(),
                token,
                jwtUtil.extractIssued(token),
                jwtUtil.extractExpiration(token));
    }
}
