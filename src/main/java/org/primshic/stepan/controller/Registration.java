package org.primshic.stepan.controller;

import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/reg")
public class Registration extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo добавить проверку на активную сессию и запретить регистрацию если она есть
        req.getRequestDispatcher(pathToView+"registration.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        UserDTO userDTO = new UserDTO(login,password);
        User user = userService.persist(userDTO).get();
        Session userSession = sessionService.startSession(user).get();
        String uuid = userSession.getId();
        Cookie cookie = new Cookie("uuid",uuid);
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath()+"/main");
    }
}
