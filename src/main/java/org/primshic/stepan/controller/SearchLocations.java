package org.primshic.stepan.controller;

import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.CookieUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/search")
public class SearchLocations extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        String name = req.getParameter("name");
        List<LocationDTO> locationDTOList = weatherAPIService.getLocationListByName(name);

        ctx.setVariable("locationList", locationDTOList);
        templateEngine.process("search", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        String sessionId = CookieUtil.getSessionIdByCookie(req.getCookies());
        Session session = sessionService.getById(sessionId).get();

        User user = session.getUser();
        String name = req.getParameter("name");
        double lat = Integer.parseInt(req.getParameter("lat"));
        double lon = Integer.parseInt(req.getParameter("lon"));
        //todo refactor
        locationService.add(user,name,lat,lon);
        templateEngine.process("main", ctx, resp.getWriter());
    }
}
