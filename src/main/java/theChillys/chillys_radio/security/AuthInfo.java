package theChillys.chillys_radio.security;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import theChillys.chillys_radio.role.Role;

import java.util.Collection;
import java.util.Set;



@Getter
public class AuthInfo implements Authentication {
    private boolean isAuthenticated; //в конструкторе не нужен ибо по умолчанию false, значение будет меняться после аутентификации
    private String username;
    private Set<Role> roles;

    public AuthInfo(String username, Set<Role> roles) {
        this.username = username;
        this.roles = roles;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthInfo authInfo)) return false;

        return username.equals(authInfo.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String getName() { // по факту это имя, но у нас совпадает с логином
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() { //Позволяет сообщить способ авторизации, для доп проверки и сразу обычно его очищают. Чаще всего пароль
        return null;
    }

    @Override
    public Object getDetails() { // IP, mac-addr или прочие доп инфо для понимания кто и откуда пришел
        return null;
    }

    @Override
    public Object getPrincipal() { // по факту логин
        return username;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }
}
