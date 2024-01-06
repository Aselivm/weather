package org.primshic.stepan.controller;

import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.util.CookieUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.WeatherUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/main")
public class UserLocations extends BaseServlet {
    private Logger log = Logger.getLogger(UserLocations.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());

        Optional<Session> userSession = SessionUtil.getCurrentSession(sessionId);
        List<LocationWeatherDTO> locationWeatherDTOList = new LinkedList<>();

        userSession.ifPresent(session -> {
            log.info("User session is present");
               User user = session.getUser();
               List<Location> locationList = locationService.getUserLocations(user);

               log.info("User locations list size: " + locationList.size());

               if (locationList.size() != 0) {
                   log.info("First object from list latitude: " + locationList.get(0).getLat());
               }

               locationWeatherDTOList.addAll(WeatherUtil.getWeatherForLocations(locationList));

               log.info("Locations converted to LocationWeather list size: " + locationWeatherDTOList.size());
            });

        ctx.setVariable("userSession", userSession);
        ctx.setVariable("locationWeatherList", locationWeatherDTOList);
        templateEngine.process("main", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int databaseId = Integer.parseInt(req.getParameter("databaseId"));
        locationService.delete(databaseId);
        resp.sendRedirect(req.getContextPath()+"/main");
    }

}
