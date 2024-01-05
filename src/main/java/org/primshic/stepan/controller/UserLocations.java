package org.primshic.stepan.controller;

import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.util.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@WebServlet(urlPatterns = "/main")
public class UserLocations extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
        List<Location> locationList=null;
        List<LocationWeatherDTO> locationWeatherDTOList = new LinkedList<>();

        Optional<Session> userSession = sessionService.getById(sessionId);

        if(userSession.isPresent()){
            locationList = userSession.get().getUser().getLocations();

            for(Location location : locationList){
                LocationWeatherDTO locationWeatherDTO =
                        weatherAPIService.getWeatherByLocation(location);
                locationWeatherDTOList.add(locationWeatherDTO);
            }
        }
        req.setAttribute("locationWeatherList",locationWeatherDTOList);
        req.getRequestDispatcher(pathToView+"main.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
        int userId = sessionService.getById(sessionId).get().getUser().getId();
        double lat = Integer.parseInt(req.getParameter("lat"));
        double lon = Integer.parseInt(req.getParameter("lon"));
        locationService.delete(userId,lat,lon);
        resp.sendRedirect(req.getContextPath()+"/main");
    }

}
