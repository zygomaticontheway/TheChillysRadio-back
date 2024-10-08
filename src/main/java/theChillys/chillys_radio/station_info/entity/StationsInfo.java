package theChillys.chillys_radio.station_info.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "station_info")
public class StationsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "type")
    private String type;

    public StationsInfo(String title, Long amount, String type) {
        this.title = title;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StationsInfo that)) return false;

        return id.equals(that.id) && title.equals(that.title) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
