package org.primshic.stepan.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.util.PropertyReaderUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;


public class WeatherAPIService {
    private static final Logger log = Logger.getLogger(WeatherAPIService.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

/*    public static void main(String[] args) {
        WeatherAPIService weatherAPIService = new WeatherAPIService();
        weatherAPIService.getLocationListByName("Москва");
    }*/
    public List<LocationDTO> getLocationListByName(String name){
        List<LocationDTO> locationList;
        String url = getWeatherAPIProperty("url_geo");
        String limit = getWeatherAPIProperty("limit");
        String appid = getWeatherAPIProperty("APIKey");
        String result = getLocationListResponse(url,name,limit,appid);
        try {
            locationList = objectMapper.readValue(result, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.warning("Error reading JSON response: "+e.getMessage());
            throw new RuntimeException(e); // todo добавить эксепшн
        }
        log.info("List of locations by name: "+locationList.toString());
        return locationList;
    }

    public LocationWeatherDTO getWeatherByLocation(Location location){
        LocationWeatherDTO locationWeatherDTO;
        BigDecimal lat = location.getLat();
        BigDecimal lon = location.getLon();
        String appid = getWeatherAPIProperty("APIKey");
        String lang = getWeatherAPIProperty("lang");
        String units = getWeatherAPIProperty("units");
        String url = getWeatherAPIProperty("url_data");

        String result = getWeatherResponse(lat,lon,url,appid,lang,units);
        try {
           locationWeatherDTO = objectMapper.readValue(result, LocationWeatherDTO.class);
        } catch (JsonProcessingException e) {
            log.warning("Error reading JSON response: "+e.getMessage());
            throw new RuntimeException(e);//todo добавить эксепшн
        }
        log.info("Weather by location info: "+locationWeatherDTO.toString());
        return locationWeatherDTO;
    }

    private String getWeatherAPIProperty(String key){
        return PropertyReaderUtil.read("weatherAPI.properties",key);
    }

    private String getWeatherResponse(BigDecimal lat,BigDecimal lon,String url,
                              String appid,String lang,String units){
        String latitude = String.valueOf(lat);
        String longitude = String.valueOf(lon);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url+"lat="+latitude+"&lon="+longitude+"&appid="+appid+"&lang="+lang+"&units="+units))
                    .GET()
                    .build();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body();

            return jsonResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String getLocationListResponse(String url, String name, String limit,String appid){
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url+"q="+name+"&limit="+limit+"&appid="+appid))
                    .GET()
                    .build();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body();

            return jsonResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
