package org.primshic.stepan.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.weather.locations.Location;
import org.primshic.stepan.weather.openWeatherAPI.LocationCoordinatesDTO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationUtil {
    public static boolean areCoordinatesEqual(Location location, LocationCoordinatesDTO dto) {
        return location.getLat().doubleValue() == dto.getLat() &&
                location.getLon().doubleValue() == dto.getLon();
    }

    public static void filterOutLocationsWithSameCoordinates(List<Location> locations, List<LocationCoordinatesDTO> dtos) {
        locations.removeIf(location ->
                dtos.stream().anyMatch(dto -> areCoordinatesEqual(location, dto))
        );
    }
}
