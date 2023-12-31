package org.primshic.stepan.model;

import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity()
@Table(name="Sessions")
public class Session {
    @Id
    @Column(name="ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne
    @JoinColumn(name="UserId")
    private User user;

    @Column(name="ExpiresAt")
    private LocalDateTime expiresAt;
}
