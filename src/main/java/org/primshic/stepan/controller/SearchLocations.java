package org.primshic.stepan.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/search")
public class SearchLocations extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        double lat = Integer.parseInt(req.getParameter("lat"));
        double lon = Integer.parseInt(req.getParameter("lon"));
        locationService.add(name,lat,lon);
        resp.sendRedirect(req.getContextPath()+"/main");
    }
}
