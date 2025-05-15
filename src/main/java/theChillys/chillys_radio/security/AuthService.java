package theChillys.chillys_radio.security;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
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
    private final Map<String, String> refreshTokenStorage = new HashMap<>(); //email, token

    public TokenResponseDto login(UserLoginDto inboundUser) throws AuthException {
        String email = inboundUser.getEmail();
        User foundUser = (User) userService.loadUserByEmail(email); //безопасное преобразование типов в User ибо там содержатся как раз все данные юзера

        //check password
        if (passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())){
            //password is correct
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);

            //сохраняем токен
            refreshTokenStorage.put(email, refreshToken);

            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthException("Incorrect password");
        }
    }

    public TokenResponseDto getNewAccessToken(String inboundRefreshToken){

        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String email = refreshClaims.getSubject();
        System.out.println("refreshClaims.getSubject() = " + email);
        String savedRefreshToken = refreshTokenStorage.get(email); //ранее сохраненный в базе токен

        //сравниваем сохраненный токен с пришедшим
        if (savedRefreshToken != null && savedRefreshToken.equals(inboundRefreshToken)){

            User foundUser = (User) userService.loadUserByEmail(email);
            String accessToken = tokenService.generateAccessToken(foundUser);

            return new TokenResponseDto(accessToken, null); //отдаем только accessToken
        } else {
            throw new RuntimeException("invalid token");
//            return new TokenResponseDto(null, null); //если токен невалидный то верну DTO без токенов
        }

    }


}
