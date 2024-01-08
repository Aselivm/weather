package org.primshic.stepan.test_cases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primshic.stepan.weather.openWeatherAPI.LocationCoordinatesDTO;
import org.primshic.stepan.weather.openWeatherAPI.WeatherDTO;
import org.primshic.stepan.weather.locations.Location;
import org.primshic.stepan.weather.openWeatherAPI.WeatherAPIService;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestOpenWeatherAPI {
    private static final String LONDON = "London";
    @Mock
    private WeatherAPIService weatherAPIService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void locationList() throws JsonProcessingException {
        List<LocationCoordinatesDTO> locationCoordinatesDTOList1 =
                objectMapper.readValue(readFileToString("weather_api_response/locationFile.json"), new TypeReference<>() {});

        assertThat(locationCoordinatesDTOList1).hasSize(5);
        when(weatherAPIService.getLocationListByName(LONDON)).thenReturn(locationCoordinatesDTOList1);

        List<LocationCoordinatesDTO> locationCoordinatesDTOList2 = weatherAPIService.getLocationListByName(LONDON);
        assertThat(locationCoordinatesDTOList2).containsExactlyInAnyOrderElementsOf(locationCoordinatesDTOList1);
    }

    @Test
    void locationWeather() throws JsonProcessingException {
        WeatherDTO weatherDTO1 =
                objectMapper.readValue(readFileToString("weather_api_response/weatherFile.json"), WeatherDTO.class);

        assertThat(weatherDTO1 != null);
        when(weatherAPIService.getWeatherByLocation(Mockito.any())).thenReturn(weatherDTO1);

        WeatherDTO weatherDTO2 = weatherAPIService.getWeatherByLocation(new Location());
        assertThat(weatherDTO1).isEqualTo(weatherDTO2);
    }

    @Test
    void containsExpectedLocation() throws JsonProcessingException {
        List<LocationCoordinatesDTO> locationCoordinatesDTOList1 =
                objectMapper.readValue(readFileToString("weather_api_response/locationFile.json"), new TypeReference<>() {});
        when(weatherAPIService.getLocationListByName(LONDON)).thenReturn(locationCoordinatesDTOList1);

        BigDecimal epsilon = BigDecimal.valueOf(0.000001);

        assertThat(weatherAPIService.getLocationListByName(LONDON)).anyMatch(location ->
                Math.abs(location.getLat() - 37.1289771) <= epsilon.doubleValue() &&
                        Math.abs(location.getLon() - (-84.0832646)) <= epsilon.doubleValue()
        );
    }
    private String readFileToString(String filePath) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }

}