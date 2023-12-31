package org.primshic.stepan.model;

import javax.persistence.*;

@Entity()
@Table(name="Users")
public class User {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Login" , nullable = false)
    private String login;

    @Column(name="Password", nullable = false)
    private String password;
}
