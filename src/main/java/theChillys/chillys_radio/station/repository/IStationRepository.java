package theChillys.chillys_radio.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import theChillys.chillys_radio.user.entity.User;

import java.util.Optional;

public interface IStationRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName(String username);
    User findAll (Long id);
}
