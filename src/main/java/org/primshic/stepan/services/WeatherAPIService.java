package org.primshic.stepan.services;

import org.primshic.stepan.dto.LocationResponse;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.util.PropertyReaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherAPIService {

    public LocationResponse getWeather(Location location){

          double lat = location.getLatitude();
          double lon = location.getLongitude();
/*        double lat = 44.34;
        double lon = 10.99;*/
          String appid = getWeatherAPIProperty("APIKey");
          String lang = getWeatherAPIProperty("lang");
          String units = getWeatherAPIProperty("units");
          String url = getWeatherAPIProperty("url");

         String result = getRequest(lat,lon,url,appid,lang,units);
         System.out.println(result);
          return null;
    }

    private String getWeatherAPIProperty(String key){
        return PropertyReaderUtil.read("weatherAPI.properties",key);
    }

    private String getRequest(double lat,double lon,String url,
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

            String requestUrl = request.uri().toString();
            System.out.println("Created URL: " + requestUrl);
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
