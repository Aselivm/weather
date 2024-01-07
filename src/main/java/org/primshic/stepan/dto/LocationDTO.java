package org.primshic.stepan.dto;

import lombok.*;
import org.primshic.stepan.model.User;

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
