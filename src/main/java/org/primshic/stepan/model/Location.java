package org.primshic.stepan.model;

import javax.persistence.*;

@Entity()
@Table(name="Locations")
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
    private double latitude;

    @Column(name="Longitude")
    private double longitude;
}
