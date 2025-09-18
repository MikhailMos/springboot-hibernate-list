package ru.example.springboot.hibernate.list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.example.springboot.hibernate.list.model.TokenDetails;
import ru.example.springboot.hibernate.list.model.UserEntity;
import ru.example.springboot.hibernate.list.model.UserRole;
import ru.example.springboot.hibernate.list.model.exception.UnauthorizedException;
import ru.example.springboot.hibernate.list.repository.UserRepository;
import ru.example.springboot.hibernate.list.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис пользователей. Обеспечивает операции по получению и регистрации пользователей,
 * а также аутентификацию и формирование токена.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    /** Репозиторий */
    private final UserRepository userRepository;
    /** Текущий энкриптер */
    private final PasswordEncoder passwordEncoder;
    /** Содержит методы работы с JSON Web Token (jwt) */
    private final JwtUtil jwtUtil;

    /**
     * Возвращает пользователя по имени пользователя.
     *
     * @param username Имя пользователя
     * @return объект UserEntity, соответствующий указанному имени пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public Optional<UserEntity> getUserByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        mapRoleToAuthority(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username)));
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthority(UserRole role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
    }


//    /**
//     * Возвращает пользователя по идентификатору.
//     *
//     * @param userId идентификатор пользователя
//     * @return объект UserEntity, соответствующий указанному идентификатору
//     * @throws UsernameNotFoundException если пользователь не найден
//     */
//    public UserEntity getUserById(Long userId) throws UsernameNotFoundException {
//        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
//
//        if (userEntityOptional.isEmpty()) {
//            throw new UsernameNotFoundException(String.format("Пользователь  c '%d' не найден", userId));
//        }
//
//        return userEntityOptional.get();
//    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param userEntity объект пользователя, который требуется зарегистрировать
     * @return зарегистрированный объект UserEntity
     */
    @Transactional
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
     * @param username идентификатор пользователя
     * @throws IllegalFormatException если пользователь не нашелся по имени
     */
    @Transactional
    public void deleteUser(Long id) throws IllegalArgumentException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.delete(optionalUser.get());
    }

    /**
     * Аутентифицирует пользователя и возвращает данные токена.
     *
     * @param username Имя пользователя
     * @param password Пароль пользователя
     * @return объект TokenDetails, содержащий детали аутентификации и токен
     */
    @Transactional
    public TokenDetails authenticate(String username, String password) {

        Optional<UserEntity> optionalUser = getUserByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", username));
        }

        UserEntity user = optionalUser.get();

        if (!user.isEnabled()) {
            throw new UnauthorizedException(String.format("Пользователь '%s' заблокирован, обратитесь к администратору.", username));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException(String.format("Неверный пароль у пользователя '%s'"), username);
        }

        String jwtoken = jwtUtil.generateToken(user);

        return new TokenDetails(
                user.getId(),
                jwtoken,
                jwtUtil.extractIssued(jwtoken),
                jwtUtil.extractExpiration(jwtoken));

    }

}
