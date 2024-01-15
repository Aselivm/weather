package org.primshic.stepan.web.servlets.search;

import lombok.*;
import org.primshic.stepan.web.auth.user.User;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRequestDTO {
    private User user;
    private String locationName;
    private BigDecimal lat;
    private BigDecimal lon;
}
