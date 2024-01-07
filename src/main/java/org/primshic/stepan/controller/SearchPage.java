package org.primshic.stepan.controller;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.user.UserLocationDTO;
import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.LocationService;
import org.primshic.stepan.services.WeatherAPIService;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.ThymeleafUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/search")
@Slf4j
public class SearchPage extends HttpServlet {
    private LocationService locationService;
    private WeatherAPIService weatherAPIService;

    @Override
    public void init() throws ServletException {
        locationService = new LocationService();
        weatherAPIService = new WeatherAPIService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Session> optionalUserSession = SessionUtil.getSessionFromCookies(req);
        try {
            String name = InputUtil.locationName(req);
            List<LocationDTO> locationDTOList = weatherAPIService.getLocationListByName(name);

            ThymeleafUtil.templateEngineProcessWithVariables("search", req, resp, new HashMap<>(){{
                put("userSession", optionalUserSession);
                put("locationList",locationDTOList);
            }});
        } catch (ApplicationException e) {
            log.error("Error processing GET request in SearchLocations: {}", e.getMessage(), e);
            ThymeleafUtil.templateEngineProcessWithVariables("search", req, resp, new HashMap<>(){{
                put("userSession", optionalUserSession);
                put("error", e.getError());
            }});
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Session> optionalUserSession = SessionUtil.getSessionFromCookies(req);
        try {

            User user = optionalUserSession.orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR)).getUser();
            String name = InputUtil.locationName(req);

            UserLocationDTO userLocationDTO = UserLocationDTO.builder()
                    .user(user)
                    .locationName(name)
                    .lat(InputUtil.getLatitude(req))
                    .lon(InputUtil.getLongitude(req))
                    .build();

            locationService.add(userLocationDTO);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            log.error("Error processing POST request in SearchLocations: {}", e.getMessage(), e);
            ThymeleafUtil.templateEngineProcessWithVariables("search", req, resp, new HashMap<>(){{
                put("userSession", optionalUserSession);
                put("error", e.getError());
            }});
        }
    }
}

