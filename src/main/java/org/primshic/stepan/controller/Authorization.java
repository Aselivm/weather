package org.primshic.stepan.controller;

import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.exception.ExceptionHandler;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/auth")
public class Authorization extends BaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
            SessionUtil.deleteSessionIfPresent(sessionId);
            ThymeleafUtil.templateEngineProcess("authorization",req,resp);
        }catch (ApplicationException e){
            ExceptionHandler.handle(resp,e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            UserDTO userDTO = InputUtil.authenticate(req);

            User user = userService.get(userDTO).orElseThrow(()->new ApplicationException(ErrorMessage.INTERNAL_ERROR));
            Session userSession = sessionService.startSession(user).orElseThrow(()->new ApplicationException(ErrorMessage.INTERNAL_ERROR));

            String uuid = userSession.getId();
            Cookie cookie = new Cookie("uuid",uuid);

            resp.addCookie(cookie);
            resp.sendRedirect(req.getContextPath()+"/main");
        }catch (ApplicationException e){
            ExceptionHandler.handle(resp,e);
        }
    }
}
