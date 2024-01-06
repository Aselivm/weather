package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.LocationService;
import org.primshic.stepan.services.WeatherAPIService;

import java.util.List;
import java.util.logging.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtil {
    private static final LocationService locationService = new LocationService();
    private static final WeatherAPIService weatherAPIService = new WeatherAPIService();
    private static Logger log = Logger.getLogger(PageUtil.class.getName());
    public static void processUserPage(Session session, List<LocationWeatherDTO> locationWeatherDTOList) {
        log.info("User session is present");
        User user = session.getUser();
        List<Location> locationList = locationService.getUserLocations(user);

        log.info("User locations list size: " + locationList.size());

        locationWeatherDTOList.addAll(weatherAPIService.getWeatherForLocations(locationList));

        log.info("Locations converted to LocationWeather list size: " + locationWeatherDTOList.size());
    }

    public static List<LocationDTO> getLocationsByName(String name){
        return weatherAPIService.getLocationListByName(name);
    }
}
