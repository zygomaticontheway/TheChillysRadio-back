package theChillys.chillys_radio.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshResponseDto that)) return false;

        return Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accessToken);
        result = 31 * result + Objects.hashCode(refreshToken);
        return result;
    }

    @Override
    public String toString() {
        return "RefreshResponseDto{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
