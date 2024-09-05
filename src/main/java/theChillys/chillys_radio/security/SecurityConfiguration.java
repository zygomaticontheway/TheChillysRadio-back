package theChillys.chillys_radio.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// аннотации для Spring Security => filter chain, цепочка фильтров, которые могут завернуть запрос до получения его Контроллером
@EnableWebSecurity
@EnableMethodSecurity

@Configuration
public class SecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable) //защита от cross site request, запрещает принимать запросы со сторонних сайтов
                .authorizeHttpRequests( //содержит настройки защиты эндпоинтов, все что тут прописано не будет, то будет заблокировано по умолчанию
                        x -> x
                                .requestMatchers(HttpMethod.GET, "/stations").permitAll() // permitAll = разрешить всем
                                .requestMatchers(HttpMethod.GET, "/").hasAnyRole("USER", "ADMIN") //hasAnyRole("USER") = разрешить только пользователям с перечисленными ролями  (да отбрасываем ROLE_)
                                .requestMatchers(HttpMethod.POST, "/").hasRole("ADMIN") //hasAnyRole("ADMIN") = разрешить только пользователям с ролью ADMIN
                                .anyRequest().authenticated() //все остальные запросы доступны только авторизованным пользователям
                ).httpBasic(Customizer.withDefaults());


        return http.build();
    }
}
