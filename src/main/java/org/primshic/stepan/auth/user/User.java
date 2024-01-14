package org.primshic.stepan.auth.user;

import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.primshic.stepan.weather.locations.Location;

import javax.persistence.*;
import java.security.AuthProvider;
import java.util.List;

@Entity()
@Table(name="Users",
        indexes = @Index(name="login_idx",columnList = "Login"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Login", nullable = false, unique = true)
    @Nationalized
    private String login;

    @Column(name="Password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;
}
