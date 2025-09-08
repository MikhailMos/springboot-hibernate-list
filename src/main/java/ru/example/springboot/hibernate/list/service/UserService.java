package ru.example.springboot.hibernate.list.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.springboot.hibernate.list.model.TokenDetails;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.model.UserRole;
import ru.example.springboot.hibernate.list.model.exception.UnauthorizedException;
import ru.example.springboot.hibernate.list.repository.UserRepository;
import ru.example.springboot.hibernate.list.util.BCryptEncoder;
import ru.example.springboot.hibernate.list.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис пользователей. Обеспечивает операции по получению и регистрации пользователей,
 * а также аутентификацию и формирование токена.
 */
@Service
public class UserService {

    /** Репозиторий */
    private final UserRepository userRepository;
    /** Содержит методы работы с JWT */
    private final JwtUtil jwtUtil;
    /** Содержит методы работы с  */
    private final BCryptEncoder bCryptEncoder;

    /** Конструктор с обязательными аргументами */
    public UserService(@Autowired UserRepository userRepository,
                       @Autowired JwtUtil jwtUtil,
                       @Autowired BCryptEncoder bCryptEncoder)
    {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.bCryptEncoder = bCryptEncoder;
    }

    /**
     * Возвращает пользователя по имени пользователя.
     *
     * @param username Имя пользователя
     * @return объект UserEntity, соответствующий указанному имени пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public UserEntity getUserByUsername(String username)  throws UsernameNotFoundException {
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
    @Transactional
    public UserEntity registerUser(UserEntity userEntity) {

        userEntity.setPassword(bCryptEncoder.encode(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(userEntity.getCreatedAt());
        userEntity.setEnabled(true);
        if (userEntity.getRole() == null) {
            userEntity.setRole(UserRole.USER);
        }

        return userRepository.save(userEntity);
    }

    /**
     * Возвращает список пользователей.
     *
     * @return список пользователей
     */
    @Transactional(readOnly = true)
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Удаляет пользователя из базы по идентификатору.
     *
     * @param id    идентификатор пользователя
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
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

        if (!bCryptEncoder.matches(password, userEntity.getPassword())) {
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
