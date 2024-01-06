package org.primshic.stepan.controller;

import org.primshic.stepan.services.LocationService;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.services.UserService;
import org.primshic.stepan.services.WeatherAPIService;

import javax.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {
    protected final UserService userService = new UserService();
    protected final SessionService sessionService = new SessionService();
    protected final LocationService locationService = new LocationService();
    protected final WeatherAPIService weatherAPIService = new WeatherAPIService();
}
