package org.primshic.stepan.controller;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.weather_api.WeatherDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.LocationService;
import org.primshic.stepan.services.WeatherAPIService;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.WebContextUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/main")
@Slf4j
public class UserPage extends HttpServlet {

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
        WebContext context = WebContextUtil.createContext(req, resp, getServletContext());
        Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);
        List<WeatherDTO> weatherDTOList = new LinkedList<>();
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
        WebContext context = WebContextUtil.createContext(req, resp, getServletContext());

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
