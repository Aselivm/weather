package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InputUtil {
    public static UserDTO authenticate(HttpServletRequest req) {
        String login = req.getParameter("login");
        if (login.length() > 100) {
            log.warn("Long login detected: {}", login);
            throw new ApplicationException(ErrorMessage.LONG_LOGIN);
        }
        String password = req.getParameter("password");
        if (password.length() > 100) {
            log.warn("Long password detected for user with login: {}", login);
            throw new ApplicationException(ErrorMessage.LONG_PASSWORD);
        }
        return new UserDTO(login, password);
    }

    public static Integer deletedLocationId(HttpServletRequest req) {
        try {
            return Integer.parseInt(req.getParameter("databaseId"));
        } catch (NumberFormatException e) {
            log.error("Error parsing location ID from request parameter: {}", req.getParameter("databaseId"), e);
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }

    public static String locationName(HttpServletRequest req) {
        String name = req.getParameter("name").trim();
        if (name.isEmpty()) {
            log.warn("Empty location name detected");
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
        return name;
    }

    public static BigDecimal getLatitude(HttpServletRequest req) {
        try {
            return new BigDecimal(req.getParameter("lat"));
        } catch (NumberFormatException e) {
            log.error("Error parsing latitude from request parameter: {}", req.getParameter("lat"), e);
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }

    public static BigDecimal getLongitude(HttpServletRequest req) {
        try {
            return new BigDecimal(req.getParameter("lon"));
        } catch (NumberFormatException e) {
            log.error("Error parsing longitude from request parameter: {}", req.getParameter("lon"), e);
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }
}
