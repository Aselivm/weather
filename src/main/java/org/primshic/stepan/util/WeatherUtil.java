package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.services.WeatherAPIService;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherUtil {
    private static final Logger log = Logger.getLogger(WeatherUtil.class.getName());
    private static final WeatherAPIService weatherAPIService = new WeatherAPIService();

    public static List<LocationWeatherDTO> getWeatherForLocations(List<Location> locationList) {
        List<LocationWeatherDTO> locationWeatherDTOList = new LinkedList<>();
        for (Location location : locationList) {
            try {
                LocationWeatherDTO locationWeatherDTO = weatherAPIService.getWeatherByLocation(location);
                locationWeatherDTOList.add(locationWeatherDTO);
            } catch (RuntimeException e) {
                //todo exception
                log.warning("Error getting weather for location {"+location.getName()+"}: {"+e.getMessage()+"}");
                throw new RuntimeException();
            }
        }
        return locationWeatherDTOList;
    }
}
