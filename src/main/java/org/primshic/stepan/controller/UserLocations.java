package org.primshic.stepan.controller;

import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.services.SessionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@WebServlet(name="search page", urlPatterns = "/main")
public class UserLocations extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        Cookie uuidCookie = null;
        List<Location> locationList=null;
        for(Cookie cookie : cookies){
            if(Objects.equals(cookie.getName(), "uuid")) {
                uuidCookie = cookie;
            }
        }
        if(uuidCookie!=null){
            Optional<Session> userSession = sessionService.getById(uuidCookie.getValue());

            if(userSession.isPresent()){
                locationList = userSession.get().getUser().getLocations();
            }
        }
        req.getRequestDispatcher(pathToView+"main.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
