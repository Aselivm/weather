package org.primshic.stepan.controller;

import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.exception.ExceptionHandler;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/main")
public class UserLocations extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<LocationWeatherDTO> locationWeatherDTOList = new LinkedList<>();
        try {
            Session userSession = SessionUtil.getSessionByReq(req);
            PageUtil.processUserPage(userSession, locationWeatherDTOList);
            ThymeleafUtil.templateEngineProcessWithVariables("search", req, resp, new HashMap<>(){{
                put("userSession", userSession);
                put("locationWeatherList",locationWeatherDTOList);
            }});
        } catch (ApplicationException e) {
            ExceptionHandler.handle(resp, e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int databaseId = InputUtil.deletedLocationId(req);
            locationService.delete(databaseId);
            resp.sendRedirect(req.getContextPath() + "/main");
        } catch (ApplicationException e) {
            ExceptionHandler.handle(resp,e);
        }

    }


}
