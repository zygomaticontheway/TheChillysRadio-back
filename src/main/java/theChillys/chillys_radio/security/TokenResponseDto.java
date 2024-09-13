package theChillys.chillys_radio.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenResponseDto that)) return false;

        return Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accessToken);
        result = 31 * result + Objects.hashCode(refreshToken);
        return result;
    }
}
