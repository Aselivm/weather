package org.primshic.stepan.web.servlets.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDTO {
    private String name;
    private String country;
    private String state;
    private BigDecimal lat;
    private BigDecimal lon;
}
