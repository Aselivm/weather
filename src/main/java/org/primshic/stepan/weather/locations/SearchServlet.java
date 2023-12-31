package org.primshic.stepan.weather.locations;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.auth.session.Session;
import org.primshic.stepan.auth.user.User;
import org.primshic.stepan.common.WeatherTrackerBaseServlet;
import org.primshic.stepan.common.exception.ApplicationException;
import org.primshic.stepan.common.exception.ErrorMessage;
import org.primshic.stepan.weather.openWeatherAPI.WeatherAPIService;
import org.primshic.stepan.weather.openWeatherAPI.LocationCoordinatesDTO;
import org.primshic.stepan.common.util.InputUtil;
import org.primshic.stepan.common.util.SessionUtil;
import org.primshic.stepan.common.util.WebContextUtil;
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
public class SearchServlet extends WeatherTrackerBaseServlet {
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
        try {
            String name = InputUtil.locationName(req);
            List<LocationCoordinatesDTO> locationCoordinatesDTOList = weatherAPIService.getLocationListByName(name);
            context.setVariable("locationList", locationCoordinatesDTOList);

        } catch (ApplicationException e) {
            log.error("Error processing GET request in SearchLocations: {}", e.getMessage(), e);
            context.setVariable("error", e.getError());
        }

        templateEngine.process("search", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Session> optionalUserSession = SessionUtil.getSessionByReq(req);
        try {

            User user = optionalUserSession.orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR)).getUser();
            String name = InputUtil.locationName(req);

            LocationDTO locationDTO = LocationDTO.builder()
                    .user(user)
                    .locationName(name)
                    .lat(InputUtil.getLatitude(req))
                    .lon(InputUtil.getLongitude(req))
                    .build();

            locationService.add(locationDTO);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            log.error("Error processing POST request in SearchLocations: {}", e.getMessage(), e);
            context.setVariable("error", e.getError());
            templateEngine.process("search", context, resp.getWriter());
        }
    }
}

