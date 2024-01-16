package org.primshic.stepan.web.servlets.search;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.util.WebContextUtil;
import org.primshic.stepan.web.auth.session.Session;
import org.primshic.stepan.web.auth.user.User;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.weather.locations.Location;
import org.primshic.stepan.weather.locations.LocationService;
import org.primshic.stepan.weather.openWeatherAPI.WeatherAPIService;
import org.primshic.stepan.weather.openWeatherAPI.LocationCoordinatesDTO;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/search")
@Slf4j
public class SearchServlet extends HttpServlet {
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
        try {
            String name = InputUtil.locationName(req);
            List<LocationCoordinatesDTO> locationCoordinatesDTOList = weatherAPIService.getLocationListByName(name);

            Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);
            List<Location> userLocations;
            if (optionalUserSession.isPresent()) {
                User user = optionalUserSession.get().getUser();
                userLocations = locationService.getUserLocations(user);

                List<Location> finalUserLocations = userLocations;
                locationCoordinatesDTOList.removeIf(dto ->
                        finalUserLocations.stream()
                                .anyMatch(userLocation ->
                                        isClose(userLocation.getLat().doubleValue(), dto.getLat()) &&
                                                isClose(userLocation.getLon().doubleValue(), dto.getLon())
                                )
                );
            }
            context.setVariable("locationList", locationCoordinatesDTOList);
        } catch (ApplicationException e) {
            log.error("Error processing GET request in SearchLocations: {}", e.getMessage(), e);
            context.setVariable("error", e.getError());
        }

        templateEngine.process("search", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = WebContextUtil.createContext(req, resp, getServletContext());
        Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);
        try {

            User user = optionalUserSession.orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR)).getUser();
            String name = InputUtil.locationName(req);

            LocationRequestDTO locationRequestDTO = LocationRequestDTO.builder()
                    .user(user)
                    .locationName(name)
                    .lat(InputUtil.getLatitude(req))
                    .lon(InputUtil.getLongitude(req))
                    .build();

            locationService.add(locationRequestDTO);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            log.error("Error processing POST request in SearchLocations: {}", e.getMessage(), e);
            context.setVariable("error", e.getError());
            templateEngine.process("search", context, resp.getWriter());
        }
    }

    private boolean isClose(double value1, double value2) {
        final double EPSILON = 0.01;
        return Math.abs(value1 - value2) < EPSILON;
    }

}

