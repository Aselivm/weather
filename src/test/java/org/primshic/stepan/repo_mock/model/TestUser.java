package org.primshic.stepan.repo_mock.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity()
@Table(name="Users",
        indexes = {@Index(name="login_idx",columnList = "Login"),
                @Index(name="password_idx",columnList = "Password")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
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
    private List<TestLocation> locations;
}
