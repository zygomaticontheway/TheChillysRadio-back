package theChillys.chillys_radio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theChillys.chillys_radio.user.entity.User;

import java.util.Optional;

public interface IDataRepository extends JpaRepository<User, Long> {
    public Optional<User> findUserByName(String name);
}
