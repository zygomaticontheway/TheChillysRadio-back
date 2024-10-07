package theChillys.chillys_radio.station.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import theChillys.chillys_radio.station.entity.Station;

import java.util.List;
import java.util.Optional;


public interface IStationRepository extends JpaRepository<Station, Long> {

    Page<Station> findAllByOrderByClickcountDesc(Pageable pageable);

    Page<Station> findAllByOrderByVotesDesc(Pageable pageable);

    Optional<Station> findByStationuuid(String stationuuid);

    Page<Station> findByNameContainingIgnoreCaseAndTagsContainingIgnoreCaseAndCountryContainsIgnoreCaseAndLanguageContainingIgnoreCase(
            String name, String tags, String country, String language, Pageable pageable);

    @Query("SELECT s FROM Station s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.tags) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.country) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.countrycode) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.state) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.language) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.languagecodes) LIKE LOWER(CONCAT('%', :search, '%')) " )
    Page<Station> searchStationsByTerm (@Param("search") String search, Pageable pageable);

}