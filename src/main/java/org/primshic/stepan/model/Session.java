package org.primshic.stepan.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity()
@Table(schema = "weather_db",name="Sessions",
        indexes = @Index(name = "expiresAt_idx",columnList = "ExpiresAt") )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @Column(name="ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne()
    @JoinColumn(name="UserId")
    private User user;

    @Column(name="ExpiresAt")
    private LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
}
