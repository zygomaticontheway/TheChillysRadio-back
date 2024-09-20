package theChillys.chillys_radio.station.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import theChillys.chillys_radio.station.entity.Station;

import java.util.List;
import java.util.Optional;


public interface IStationRepository extends JpaRepository<Station, Long> {
    List<Station> findAllByOrderByClickcountDesc();
    List<Station> findAllByOrderByVotesDesc();
    Optional<Station> findByStationuuid(String stationuuid);
    List<Station> findStationByNameOrTagsOrCountryOrLanguage(String name, String tags, String country, String language);

}