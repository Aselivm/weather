package org.primshic.stepan.weather.locations;

import lombok.*;
import org.primshic.stepan.auth.user.User;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDTO {
    private User user;
    private String locationName;
    private BigDecimal lat;
    private BigDecimal lon;
}
