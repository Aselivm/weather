package org.primshic.stepan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

public class TestLocation {
    @Entity()
    @Table(schema = "weather_test",name="Locations")
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
}
