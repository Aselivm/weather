package org.primshic.stepan.controller;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.location_weather.WeatherDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ExceptionHandler;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.ThymeleafUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/main")
@Slf4j
public class UserPage extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<WeatherDTO> weatherDTOList = new LinkedList<>();
        try {
            Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);

            if (optionalUserSession.isPresent()) {
                User user = optionalUserSession.get().getUser();
                List<Location> locationList = locationService.getUserLocations(user);
                weatherDTOList.addAll(weatherAPIService.getWeatherForLocations(locationList));
            }

            ThymeleafUtil.templateEngineProcessWithVariables("main", req, resp, new HashMap<>(){{
                put("userSession", optionalUserSession);
                put("locationWeatherList", weatherDTOList);
            }});
        } catch (ApplicationException e) {
            log.error("Error processing GET request in UserLocations: {}", e.getMessage(), e);
            ExceptionHandler.handle(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int databaseId = InputUtil.deletedLocationId(req);
            locationService.delete(databaseId);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            log.error("Error processing POST request in UserLocations: {}", e.getMessage(), e);
            ExceptionHandler.handle(resp, e);
        }
    }
}
