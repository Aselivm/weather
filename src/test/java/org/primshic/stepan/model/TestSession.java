package org.primshic.stepan.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity()
@Table(name="Sessions",schema = "weather_test",
        indexes = @Index(name = "expiresAt_idx",columnList = "ExpiresAt") )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestSession {
    @Id
    @Column(name="ID")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserId")
    private TestUser user;

    @Column(name = "ExpiresAt", columnDefinition = "DATETIME DEFAULT (CURRENT_TIMESTAMP + INTERVAL 1 HOUR)")
    private LocalDateTime expiresAt;
}
