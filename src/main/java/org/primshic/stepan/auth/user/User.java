package org.primshic.stepan.auth.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primshic.stepan.weather.locations.Location;

import javax.persistence.*;
import java.util.List;

@Entity()
@Table(schema = "weather_db",name="Users",
        indexes = {@Index(name="login_idx",columnList = "Login"),
        @Index(name="password_idx",columnList = "Password")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Login" , nullable = false, unique = true)

    private String login;

    @Column(name="Password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;
}
