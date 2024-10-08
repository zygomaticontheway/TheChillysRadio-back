package theChillys.chillys_radio.station_info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import theChillys.chillys_radio.station_info.entity.StationsInfo;


public interface IStationsInfoRepository extends JpaRepository<StationsInfo, Long> {

}
