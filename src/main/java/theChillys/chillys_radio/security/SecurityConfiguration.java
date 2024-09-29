package theChillys.chillys_radio.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// аннотации для Spring Security => filter chain, цепочка фильтров, которые могут завернуть запрос до получения его Контроллером
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor

@Configuration
public class SecurityConfiguration {

    private final TokenFilter filter;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http



                .csrf(AbstractHttpConfigurer::disable) //защита от cross site request, запрещает принимать запросы со сторонних сайтов
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //говорим spring security чтобы сохранял сессию
                .httpBasic(AbstractHttpConfigurer::disable)//отключаем basic authorisation
                .authorizeHttpRequests( //содержит настройки защиты эндпоинтов, все что тут прописано не будет, то будет заблокировано по умолчанию
                        (x) -> x
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")// permitAll = разрешить всем
                                .requestMatchers(HttpMethod.GET, "/users/{id}/favorites").hasAnyRole("USER", "ADMIN") //hasAnyRole("USER") = разрешить только пользователям с перечисленными ролями  (да отбрасываем ROLE_)
                                .requestMatchers(HttpMethod.POST, "/users").permitAll() //.hasAnyRole("USER", "ADMIN") //hasRole("ADMIN") = разрешить только пользователям с ролью ADMIN
                                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/ivan-stations").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/ivan-stations/{stationuuid}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/click/{stationuuid}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/vote/{stationuuid}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/stations").permitAll()
                                .anyRequest().authenticated() //все остальные запросы доступны только авторизованным пользователям
                ).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class); //добавили фильтр

        return http.build();
    }
}
