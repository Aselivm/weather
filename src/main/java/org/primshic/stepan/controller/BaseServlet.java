package org.primshic.stepan.controller;

import org.primshic.stepan.services.LocationService;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.services.UserService;

import javax.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {
    protected String pathToView = "WEB-INF/view/";
    protected UserService userService = new UserService();
    protected SessionService sessionService = new SessionService();
    protected LocationService locationService = new LocationService();
}
