package org.primshic.stepan.common;

import org.primshic.stepan.common.util.WebContextUtil;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class WeatherTrackerBaseServlet extends HttpServlet {

    protected WebContext context;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = WebContextUtil.createContext(req, resp, getServletContext());
        super.service(req, resp);
    }
}
