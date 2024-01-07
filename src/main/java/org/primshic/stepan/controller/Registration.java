package org.primshic.stepan.controller;

import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.user.UserDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.exception.ExceptionHandler;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.LocationService;
import org.primshic.stepan.services.SessionService;
import org.primshic.stepan.services.UserService;
import org.primshic.stepan.services.WeatherAPIService;
import org.primshic.stepan.util.CookieUtil;
import org.primshic.stepan.util.InputUtil;
import org.primshic.stepan.util.SessionUtil;
import org.primshic.stepan.util.ThymeleafUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/reg")
@Slf4j
public class Registration extends HttpServlet {
    private UserService userService;

    private SessionService sessionService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        sessionService = new SessionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
            SessionUtil.deleteSessionIfPresent(sessionId);
            ThymeleafUtil.templateEngineProcess("registration", req, resp);
        } catch (ApplicationException e) {
            log.error("Error processing GET request in Registration: {}", e.getMessage(), e);
            ExceptionHandler.handle(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserDTO userDTO = InputUtil.authenticate(req);

            User user = userService.persist(userDTO).orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));
            Session userSession = sessionService.startSession(user).orElseThrow(() -> new ApplicationException(ErrorMessage.INTERNAL_ERROR));

            Cookie cookie = CookieUtil.createUUIDCookie(userSession);
            resp.addCookie(cookie);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            log.error("Error processing POST request in Registration: {}", e.getMessage(), e);
            ExceptionHandler.handle(resp, e);
        }
    }
}

