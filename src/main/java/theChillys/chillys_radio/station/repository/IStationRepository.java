package theChillys.chillys_radio.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theChillys.chillys_radio.station.entity.Station;

public interface IStationRepository extends JpaRepository<Station, Long> {
}
