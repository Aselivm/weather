package org.primshic.stepan.repo_mock.model;

import lombok.*;

import javax.persistence.*;

@Entity()
@Table(name="Locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestLocation {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private TestUser user;

    @Column(name="Latitude")
    private double lat;

    @Column(name="Longitude")
    private double lon;
    }

