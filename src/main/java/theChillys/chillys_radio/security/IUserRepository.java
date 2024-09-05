package theChillys.chillys_radio.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByName(String name);
}
