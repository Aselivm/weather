package org.primshic.stepan.controller;

import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
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
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/auth")
public class Authorization extends BaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //todo добавить проверку на активную сессию и запретить авторизацию если она есть
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
        SessionUtil.deleteSessionIfPresent(sessionId);

        templateEngine.process("authorization", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        UserDTO userDTO = new UserDTO(login,password);
        User user = userService.get(userDTO).get();
        Session userSession = sessionService.startSession(user).get();
        String uuid = userSession.getId();
        Cookie cookie = new Cookie("uuid",uuid);

        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath()+"/main");
    }
}
