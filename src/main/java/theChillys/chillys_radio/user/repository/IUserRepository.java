package theChillys.chillys_radio.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theChillys.chillys_radio.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    static List<User> findUsersByNameOrEmail(String name, String email) {
    }

    public Optional<User> findUserByName(String name);

   public List<User> findByNameContainingOrEmailContaining(String name, String email);

}

