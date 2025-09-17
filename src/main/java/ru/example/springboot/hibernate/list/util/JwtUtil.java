package ru.example.springboot.hibernate.list.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.example.springboot.hibernate.list.model.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Утилита для работы с JWT-токенами. Отвечает за генерацию и валидацию JST
 * Предоставляет методы по извлечению информации из токена, генерации и валидации токенов.
 */
@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret}")
    private String secret;


    /**
     * Извлекает имя пользователя из токена.
     *
     * @param token JWT-токен
     * @return имя пользователя
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Извлекает время выдачи токена.
     *
     * @param token JWT-токен
     * @return дата выдачи токена
     */
    public Date extractIssued(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    /**
     * Извлекает время истечения срока действия токена.
     *
     * @param token JWT-токен
     * @return дата истечения срока действия токена
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает произвольное утверждение (claim) из токена, используя заданный резолвер.
     *
     * @param token JWT-токен
     * @param claimsResolver функция, которая преобразует Claims в нужный тип
     * @param <T> ожидаемый тип возвращаемого значения
     * @return значение утверждения, полученное с помощью claimsResolver
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Извлекает все утверждения (claims) из токена.
     *
     * @param token JWT-токен
     * @return объект Claims, содержащий все утверждения токена
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Проверяет, истек ли срок действия токена.
     *
     * @param token JWT-токен
     * @return true, если токен просрочен, иначе false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Генерирует токен для заданного пользователя.
     *
     * @param user объект пользователя, для которого создается токен
     * @return сгенерированный JWT-токен
     */
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("userId", user.getId());
        }};

        return createToken(claims, user.getUsername());
    }

    /**
     * Создает токен с указанными утверждениями и subject (идентификатором пользователя).
     *
     * @param claims список утверждений (claims)
     * @param subject субъект токена (имя пользователя)
     * @return созданный токен в виде строки
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Long curentTimeInMilles = System.currentTimeMillis();
        Long expirationTimeInMilles = expirationInSeconds * 1000L;
        Date createdDate = new Date(curentTimeInMilles);
        Date expiratedDate = new Date(curentTimeInMilles + expirationTimeInMilles);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expiratedDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    /**
     * Проверяет токен
     *
     * @param token Jwt-токен
     * @return true, если токен валиден, иначе false
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Проверяет токен по указанному пользователю.
     *
     * @param token JWT-токен
     * @param userEntity пользователь, для которого выполняется валидация
     * @return true, если токен валиден для данного пользователя, иначе false
     */
    public Boolean validateToken(String token, UserEntity userEntity) {
        final String username = extractUsername(token);
        return (username.equals(userEntity.getUsername()) && !isTokenExpired(token));
    }
}
