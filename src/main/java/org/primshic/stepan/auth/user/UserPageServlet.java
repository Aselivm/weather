package org.primshic.stepan.auth.user;

import ch.qos.logback.core.util.ExecutorServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.auth.session.Session;
import org.primshic.stepan.common.WeatherTrackerBaseServlet;
import org.primshic.stepan.common.exception.ApplicationException;
import org.primshic.stepan.weather.locations.LocationService;
import org.primshic.stepan.weather.locations.Location;
import org.primshic.stepan.weather.openWeatherAPI.WeatherAPIService;
import org.primshic.stepan.weather.openWeatherAPI.WeatherDTO;
import org.primshic.stepan.common.util.InputUtil;
import org.primshic.stepan.common.util.SessionUtil;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@WebServlet(urlPatterns = "/main")
@Slf4j
public class UserPageServlet extends WeatherTrackerBaseServlet {

    private LocationService locationService;
    private WeatherAPIService weatherAPIService;

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        locationService = (LocationService) servletContext.getAttribute("locationService");
        weatherAPIService = (WeatherAPIService) servletContext.getAttribute("weatherAPIService");
        templateEngine = (TemplateEngine) servletContext.getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);
        try {
            if (optionalUserSession.isPresent()) {
                User user = optionalUserSession.get().getUser();
                List<Location> locationList = locationService.getUserLocations(user);
                List<WeatherDTO> weatherDTOList = weatherAPIService.getWeatherDataForLocations(locationList);
                context.setVariable("locationWeatherList",weatherDTOList);
            }
        } catch (ApplicationException e) {
            context.setVariable("error",e.getError());
            log.error("Error processing GET request in UserLocations: {}", e.getMessage(), e);;
        }
        templateEngine.process("main", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int databaseId = InputUtil.deletedLocationId(req);
            locationService.delete(databaseId);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            log.error("Error processing POST request in UserLocations: {}", e.getMessage(), e);
            context.setVariable("error", e.getError());
            templateEngine.process("main", context, resp.getWriter());
        }
    }
}
