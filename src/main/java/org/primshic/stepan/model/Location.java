package org.primshic.stepan.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity()
@Table(name="Locations")
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

    @Column(name="Latitude")
    private double lat;

    @Column(name="Longitude")
    private double lon;
}
