package org.primshic.stepan.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.auth.user.UserDTO;
import org.primshic.stepan.common.exception.ApplicationException;
import org.primshic.stepan.common.exception.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InputUtil {
    public static UserDTO authenticate(HttpServletRequest req) {
        String login = req.getParameter("login");
        validateLogin(login);

        String password = req.getParameter("password");
        validatePassword(password);

        return new UserDTO(login, password);
    }

    private static void validateLogin(String login) {
        if (login.length() > 100) {
            log.warn("Long login detected: {}", login);
            throw new ApplicationException(ErrorMessage.LONG_LOGIN);
        }
        if (login.contains(" ")) {
            log.warn("Login contains spaces: {}", login);
            throw new ApplicationException(ErrorMessage.LOGIN_CONTAINS_SPACES);
        }
    }

    private static void validatePassword(String password) {
        if (password.length() > 100) {
            log.warn("Long password detected: {}", password);
            throw new ApplicationException(ErrorMessage.LONG_PASSWORD);
        }
        if (password.contains(" ")) {
            log.warn("Password contains spaces");
            throw new ApplicationException(ErrorMessage.PASSWORD_CONTAINS_SPACES);
        }
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
        String name = req.getParameter("name");;
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
