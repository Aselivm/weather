package org.primshic.stepan.controller;

import org.primshic.stepan.dto.UserLocationDTO;
import org.primshic.stepan.dto.location_weather.LocationDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ExceptionHandler;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.util.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/search")
public class SearchLocations extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     try{
         Session userSession = SessionUtil.getSessionByReq(req);
         String name = InputUtil.locationName(req);
         List<LocationDTO> locationDTOList = PageUtil.getLocationsByName(name);

         ThymeleafUtil.templateEngineProcessWithVariables("search", req, resp, new HashMap<>(){{
             put("userSession", userSession);
             put("locationList",locationDTOList);
         }});
     }catch (ApplicationException e){
         ExceptionHandler.handle(resp, e);
     }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            Session userSession = SessionUtil.getSessionByReq(req);

            User user = userSession.getUser();
            String name = InputUtil.locationName(req);

            UserLocationDTO userLocationDTO = UserLocationDTO.builder()
                    .user(user)
                    .locationName(name)
                    .lat(InputUtil.getLatitude(req))
                    .lon(InputUtil.getLongitude(req))
                    .build();

            locationService.add(userLocationDTO);
            resp.sendRedirect(req.getContextPath()+"/main");
        }catch (ApplicationException e){
            ExceptionHandler.handle(resp, e);
        }
    }
}
