package org.primshic.stepan.weather.locations;

import lombok.*;
import org.primshic.stepan.auth.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity()
@Table(schema = "weather_db",name="Locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private User user;

    @Column(name = "Latitude", precision = 11, scale = 8)
    private BigDecimal lat;

    @Column(name = "Longitude", precision = 11, scale = 8)
    private BigDecimal lon;
}
