package org.primshic.stepan.controller;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.location_weather.WeatherDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.LocationService;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.services.WeatherAPIService;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.ThymeleafUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/main")
@Slf4j
public class UserPage extends HttpServlet {

    private LocationService locationService;
    private WeatherAPIService weatherAPIService;

    private SessionService sessionService;

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        locationService = (LocationService) getServletConfig().getServletContext().getAttribute("locationService");
        weatherAPIService = (WeatherAPIService) getServletConfig().getServletContext().getAttribute("weatherAPIService");
        sessionService = (SessionService) getServletConfig().getServletContext().getAttribute("sessionService");
        templateEngine = (TemplateEngine) getServletConfig().getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, getServletContext());

        List<WeatherDTO> weatherDTOList = new LinkedList<>();
        Optional<Session> optionalUserSession = SessionUtil.getSessionFromCookies(req,sessionService);
        context.setVariable("userSession",optionalUserSession);
        try {

            if (optionalUserSession.isPresent()) {
                User user = optionalUserSession.get().getUser();
                List<Location> locationList = locationService.getUserLocations(user);
                weatherDTOList.addAll(weatherAPIService.getWeatherForLocations(locationList));
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
        WebContext context = new WebContext(req, resp, getServletContext());

        Optional<Session> optionalUserSession = SessionUtil.getSessionFromCookies(req,sessionService);
        context.setVariable("userSession", optionalUserSession);
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
