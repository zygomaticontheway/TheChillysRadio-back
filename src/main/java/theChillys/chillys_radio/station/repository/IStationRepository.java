package theChillys.chillys_radio.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.user.entity.User;

import java.util.List;
import java.util.Optional;


public interface IStationRepository extends JpaRepository<Station, Long> {
    @Query("SELECT s FROM Station s ORDER BY s.clickcount DESC")
    List<Station> getStationsByTopClicks();

    @Query("SELECT s FROM Station s ORDER BY s.votes DESC")
    List<Station> getStationsByTopVotesAtSource();

}