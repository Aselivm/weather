package org.primshic.stepan.weather.openWeatherAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.weather.locations.Location;
import org.primshic.stepan.common.exception.ApplicationException;
import org.primshic.stepan.common.exception.ErrorMessage;
import org.primshic.stepan.common.util.PropertyReaderUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class WeatherAPIService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String apiKey;

    public WeatherAPIService(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<LocationCoordinatesDTO> getLocationListByName(String name) {
        String url = getWeatherAPIProperty("url_geo");
        String limit = getWeatherAPIProperty("limit");
        String request = buildLocationListRequest(url, name, limit, apiKey);
        log.info("Calling getLocationListByName with URL: {}",request);
        String result = sendHttpRequest(request);
        return parseLocationListResponse(result);
    }

    public WeatherDTO getWeatherByLocation(Location location) {
        String lang = getWeatherAPIProperty("lang");
        String units = getWeatherAPIProperty("units");
        String url = getWeatherAPIProperty("url_data");
        String request = buildWeatherRequest(location.getLat(), location.getLon(), url, apiKey, lang, units);
        log.info("Calling getLocationListByName with URL: {}",request);
        String result = sendHttpRequest(request);
        WeatherDTO weatherDTO = parseWeatherResponse(result);
        weatherDTO.setDatabaseId(location.getId());
        return weatherDTO;
    }

    public List<WeatherDTO> getWeatherDataForLocations(List<Location> locationList) {
        List<WeatherDTO> weatherDTOList = new LinkedList<>();
        List<Future<?>> futures = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(locationList.size());
        for (int i = 0; i < locationList.size(); i++) {
            int finalI = i;
            futures.add(executorService.submit(() -> {
                Location temp = locationList.get(finalI);
                WeatherDTO weatherResponse = getWeatherByLocation(temp);
                weatherDTOList.add(weatherResponse);
            }));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error waiting for task completion: {}", e.getMessage(), e);
                throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
            }
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
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
            throw new ApplicationException(ErrorMessage.OPEN_WEATHER_ERROR);
        }
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new ApplicationException(ErrorMessage.OPEN_WEATHER_ERROR);
        }
    }

    private List<LocationCoordinatesDTO> parseLocationListResponse(String result) {
        try {
            return objectMapper.readValue(result, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.warn("Error reading JSON response for location list: {}", e.getMessage());
            throw new ApplicationException(ErrorMessage.OPEN_WEATHER_ERROR);
        }
    }

    private WeatherDTO parseWeatherResponse(String result) {
        try {
            return objectMapper.readValue(result, WeatherDTO.class);
        } catch (JsonProcessingException e) {
            log.warn("Error reading JSON response for weather: {}", e.getMessage());
            throw new ApplicationException(ErrorMessage.OPEN_WEATHER_ERROR);
        }
    }

    private String getWeatherAPIProperty(String key) {
        return PropertyReaderUtil.read("weatherAPI.properties", key);
    }
}