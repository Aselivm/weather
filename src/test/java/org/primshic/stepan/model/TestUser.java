package org.primshic.stepan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity()
@Table(schema = "weather_test",name="Users",
        indexes = {@Index(name="login_idx",columnList = "Login"),
                @Index(name="password_idx",columnList = "Password")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestUser {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Login" , nullable = false)

    private String login;

    @Column(name="Password", nullable = false)
    private String password;

    @Transient
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;
}
