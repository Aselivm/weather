package org.primshic.stepan.controller;

import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/auth")
public class Authorization extends BaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo добавить проверку на активную сессию и запретить авторизацию если она есть
        req.getRequestDispatcher(pathToView+"authorisation.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user = userService.get(login,password);
        Session userSession = sessionService.startSession(user);
        String uuid = userSession.getId();
        Cookie cookie = new Cookie("uuid",uuid);
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath()+"/main");
    }
}
