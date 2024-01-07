package org.primshic.stepan.weather.openWeatherAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.weather.locations.Location;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.util.PropertyReaderUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class WeatherAPIService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<LocationCoordinatesDTO> getLocationListByName(String name) {
        String url = getWeatherAPIProperty("url_geo");
        String limit = getWeatherAPIProperty("limit");
        String appid = getWeatherAPIProperty("APIKey");
        String request = buildLocationListRequest(url, name, limit, appid);
        log.info("Calling getLocationListByName with URL: {}",request);
        String result = sendHttpRequest(request);
        return parseLocationListResponse(result);
    }

    public WeatherDTO getWeatherByLocation(Location location) {
        String appid = getWeatherAPIProperty("APIKey");
        String lang = getWeatherAPIProperty("lang");
        String units = getWeatherAPIProperty("units");
        String url = getWeatherAPIProperty("url_data");
        String request = buildWeatherRequest(location.getLat(), location.getLon(), url, appid, lang, units);
        log.info("Calling getLocationListByName with URL: {}",request);
        String result = sendHttpRequest(request);
        return parseWeatherResponse(result);
    }

    public List<WeatherDTO> getWeatherForLocations(List<Location> locationList) {
        List<WeatherDTO> weatherDTOList = new LinkedList<>();
        for (Location location : locationList) {
            try {
                WeatherDTO weatherDTO = getWeatherByLocation(location);
                weatherDTO.setDatabaseId(location.getId());
                weatherDTOList.add(weatherDTO);
            } catch (RuntimeException e) {
                log.warn("Error getting weather for location {}: {}", location.getName(), e.getMessage());
            }
        }
        return weatherDTOList;
    }

    private String buildLocationListRequest(String url, String name, String limit, String appid) {
        return url + "q=" + name + "&limit=" + limit + "&appid=" + appid;
    }

    private String buildWeatherRequest(BigDecimal lat, BigDecimal lon, String url, String appid, String lang, String units) {
        return url + "lat=" + lat + "&lon=" + lon + "&appid=" + appid + "&lang=" + lang + "&units=" + units;
    }

    private String sendHttpRequest(String requestUrl) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(requestUrl))
                    .GET()
                    .build();

        } catch (URISyntaxException e) {
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }

    private List<LocationCoordinatesDTO> parseLocationListResponse(String result) {
        try {
            return objectMapper.readValue(result, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.warn("Error reading JSON response for location list: {}", e.getMessage());
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }

    private WeatherDTO parseWeatherResponse(String result) {
        try {
            return objectMapper.readValue(result, WeatherDTO.class);
        } catch (JsonProcessingException e) {
            log.warn("Error reading JSON response for weather: {}", e.getMessage());
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }

    private String getWeatherAPIProperty(String key) {
        return PropertyReaderUtil.read("weatherAPI.properties", key);
    }
}