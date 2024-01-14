package org.primshic.stepan.weather.openWeatherAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.primshic.stepan.weather.locations.Location;
import org.primshic.stepan.common.exception.ApplicationException;
import org.primshic.stepan.common.exception.ErrorMessage;
import org.primshic.stepan.common.util.PropertyReaderUtil;
import org.primshic.stepan.weather.locations.search.LocationResponseDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        log.info("Calling getLocationListByName with URL: {}", request);
        String result = sendHttpRequest(request);
        List<LocationCoordinatesDTO> locationCoordinatesDTO = parseLocationListResponse(result);

        //todo Удалить цикл
        for (LocationCoordinatesDTO locationDTO : locationCoordinatesDTO) {
            log.info("Location Name: {}", locationDTO.getName());
            log.info("Location Latitude: {}", locationDTO.getLat());
            log.info("Location Longitude: {}", locationDTO.getLon());
        }

        return locationCoordinatesDTO;
    }

    public WeatherDTO getWeatherByLocation(Location location) {
        String lang = getWeatherAPIProperty("lang");
        String units = getWeatherAPIProperty("units");
        String url = getWeatherAPIProperty("url_data");
        String request = buildWeatherRequest(location.getLat(), location.getLon(), url, apiKey, lang, units);
        log.info("Calling getLocationListByName with URL: {}", request);
        String result = sendHttpRequest(request);
        WeatherDTO weatherDTO = parseWeatherResponse(result);
        log.info("WeatherDTO Name: {}", weatherDTO.getName());
        return weatherDTO;
    }

    public List<WeatherDTO> getWeatherDataForLocations(List<Location> locationList) {
        List<WeatherDTO> weatherDTOList = new LinkedList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(locationList.size());
        List<Callable<WeatherDTO>> callables = new ArrayList<>();

        for (Location location : locationList) {
            callables.add(() -> {
                WeatherDTO weatherDTO = getWeatherByLocation(location);
                weatherDTO.setName(location.getName());
                return weatherDTO;
            });
        }

        try {
            List<Future<WeatherDTO>> futures = executorService.invokeAll(callables);

            for (Future<WeatherDTO> future : futures) {
                try {
                    weatherDTOList.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        } finally {
            executorService.shutdown();
        }

        return weatherDTOList;
    }

    private String buildLocationListRequest(String url, String name, String limit, String appid) {
        return url + "?q=" + name + "&limit=" + limit + "&appid=" + appid;
    }

    private String buildWeatherRequest(BigDecimal lat, BigDecimal lon, String url, String appid, String lang, String units) {
        return url + "?lat=" + lat + "&lon=" + lon + "&appid=" + appid + "&lang=" + lang + "&units=" + units;
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
            throw new ApplicationException(ErrorMessage.BAD_REQUEST);
        }

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            String responseBody = response.body();
            log.info("Response Body: {}", responseBody);
            return responseBody;
        } catch (IOException | InterruptedException e) {
            throw new ApplicationException(ErrorMessage.OPEN_WEATHER_ERROR);
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