package theChillys.chillys_radio.security;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.service.IUserService;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class AuthService {
    private final IUserService userService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;


    private final Map<String, String> refreshTokenStorage = new HashMap<>(); //name, token

    public TokenResponseDto login(UserLoginDto inbuondUser) throws AuthException {
        String username = inbuondUser.getName();
        User foundUser = (User) userService.loadUserByUsername(username); //безопасное преобразование типов в User ибо там содержатся как раз все данные юзера

        //check password
        if (passwordEncoder.matches(inbuondUser.getPassword(), foundUser.getPassword())){
            //password is correct
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);

            //сохраняем токен
            refreshTokenStorage.put(username, refreshToken);

            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthException("Incorrect password");
        }
    }

    public TokenResponseDto getNewAccessToken(String inboundRefreshToken){

        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String savedRefreshToken = refreshTokenStorage.get(username); //ранее сохраненный в базе токен

        //сравниваем сохраненный токен с пришедшим
        if (savedRefreshToken != null && savedRefreshToken.equals(inboundRefreshToken)){

            User foundUser = (User) userService.loadUserByUsername(username);
            String accessToken = tokenService.generateAccessToken(foundUser);

            return new TokenResponseDto(accessToken, null); //отдаем только accessToken
        } else {
            return new TokenResponseDto(null, null); //если токен невалидный то верну DTO без токенов
            //но можно вернуть и exception
//            throw new RuntimeException();
        }

    }

 //   public void logout(String token) {
 //       Claims claims = tokenService.getRefreshClaims(token);
 //      String username = claims.getSubject();

  //      refreshTokenStorage.remove(username);
   // }

    public void logout(String refreshToken) {
        // Проверить валидность refresh-токена
        Claims claims = tokenService.getRefreshClaims(refreshToken);

        if (claims != null) {
            // Извлечь информацию о пользователе из токена
            String userId = claims.getSubject();

            // Удалить refresh-токен пользователя
            refreshTokenStorage.remove(userId);

            // Здесь можно добавить дополнительные действия, например, логирование
            System.out.println("User " + userId + " has logged out.");
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }


}
