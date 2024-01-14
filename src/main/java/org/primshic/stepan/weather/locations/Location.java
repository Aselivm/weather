package org.primshic.stepan.weather.locations;

import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.primshic.stepan.auth.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity()
@Table(name="Locations",uniqueConstraints = @UniqueConstraint(columnNames = {"Latitude", "Longitude", "UserId"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Name")
    @Nationalized
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private User user;

    @Column(name = "Latitude", precision = 30, scale = 20)
    private BigDecimal lat;

    @Column(name = "Longitude", precision = 30, scale = 20)
    private BigDecimal lon;

}
