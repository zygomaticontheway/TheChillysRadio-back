package theChillys.chillys_radio.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {
    private final TokenService service;

    // вклиниваемся в цепочку фильтров своим фильтром, их подсовывает сам spring
    @Override
    public void doFilter(ServletRequest request, //запрос на сервер
                         ServletResponse response, //ответ сервера который tomcat отправит обратно на front
                         FilterChain filterChain //цепочка фильтров, выполняемых после моего
    ) throws IOException, ServletException {


        //получаем токен
        String token = getTokenFromRequest((HttpServletRequest) request);

        //если токен валидный, то пропускаем запрос
        if (token != null && service.validateAccessToken(token)) {

            // берем инфу из токена
            Claims claims = service.getAccessClaims(token);

            //мапим claims так чтобы получить авторизационную инфу
            AuthInfo authInfo = service.mapClaimsToAuthInfo(claims);

            //установили признак аутентифицированности пользователя
            authInfo.setAuthenticated(true);

            //кладем вышеустановленное в контекст
            SecurityContextHolder.getContext().setAuthentication(authInfo);
        }
            filterChain.doFilter(request, response);
    }

    //вспомогательный метод для доставания токена из request
    private String getTokenFromRequest(HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        //токен придет не в чистом виде, а в виде Bearer ouoeyweiywqojkzxclXLZJKlnc...
        //нужно обрезать лишнее и извлечь токен
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
