package theChillys.chillys_radio.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theChillys.chillys_radio.station.entity.Station;


import java.util.List;
import java.util.Optional;


public interface IStationRepository extends JpaRepository<Station, Long> {
    List<Station> findAllByOrderByClickcountDesc();
    List<Station> findAllByOrderByVotesDesc();
    Optional<Station> findByStationuuid(String stationuuid);

}