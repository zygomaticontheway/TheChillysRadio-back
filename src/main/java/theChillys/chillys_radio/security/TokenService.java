package theChillys.chillys_radio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.role.IRoleRepository;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.user.entity.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

// генератор https://www.devglan.com/online-tools/hmac-sha256-online
//oxCIJkAR/pepyVVpChYM3UhUfsjY8jfkSyqp7Do9xFc=

//refresh SHA256 Base64.application.properties


@Service
public class TokenService {
    public static final int ACCESS_DAYS = 7;
    public static final int REFRESH_DAYS = 30;
    private SecretKey accessKey;
    private SecretKey refreshKey;
    private final IRoleRepository roleRepository;

    // @Value - аннотация для получения значений из application.properties, это чувствительные данные, которые хранятся только внутри системы
    public TokenService(@Value("${key.access}") String accessPhrase, // ${key.access} придуманные имена из головы, такие же должны быть в application.properties
                        @Value("${key.refresh}") String refreshPhrase,
                        @Autowired IRoleRepository roleRepository) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));
        this.roleRepository = roleRepository;
    }

    private Date getExpirationDate(int days) {
        //определить до когда токен будет действовать
        LocalDateTime currentDate = LocalDateTime.now(); //получаем текущую дату
        Instant expiration = currentDate.plusDays(days)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date expirationDate = Date.from(expiration);//Date.from из пакета java.util
        return expirationDate;
    }

    public String generateAccessToken(User user) {

        Date expirationDate = getExpirationDate(ACCESS_DAYS);

        //генерим токен с помощью библиотеки Jwts, закладываем сюда нужную нам инфу
        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .claim("roles", user.getRoles())//ключ-значение внутри сгенеренного токена
                .claim("name", user.getUsername())
                .signWith(accessKey) // подписываем
                .compact(); //возвращает строку собственно токена
    }

    public String generateRefreshToken(User user) {

        Date expirationDate = getExpirationDate(REFRESH_DAYS);

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    //валидация токена, приватный метод-прослойка
    private boolean validateToken(String token, SecretKey key) {

        try {
            //настройка разборщика токена = получи claims
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
//            System.out.println("Check token validation");
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateAccessToken(String accessToken){
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken){
        return validateToken(refreshToken, refreshKey);
    }

    private Claims getClaims (String token, SecretKey key){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims getAccessClaims(String accessToken){
        return getClaims(accessToken, accessKey);
    }

    public Claims getRefreshClaims(String refreshToken){
        return getClaims(refreshToken, refreshKey);
    }

    public AuthInfo mapClaimsToAuthInfo (Claims claims){

        String username = claims.getSubject(); //при формировании
        claims.get("roles");

//        List<LinkedHashMap<String, String>> - это нужно запомнить, там такая структура данных
//        в формате ключ<String> значение <String>
        List<LinkedHashMap<String, String>> rolesList = (List<LinkedHashMap<String, String>>) claims.get("roles");
        Set<Role> roles = new HashSet<>();
        for (LinkedHashMap<String, String> roleEntry : rolesList){
            String roleTitle = roleEntry.get("authority"); //так называется ключ, надо просто запомнить
            Role role = roleRepository.findRoleByTitle(roleTitle);
            System.out.println("Роль : " + role.getTitle());
            if(role != null){
                roles.add(role);
            }
        }
        return new AuthInfo(username, roles);
    }

}
