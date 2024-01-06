package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.primshic.stepan.dto.account.UserDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InputUtil {
    public static UserDTO authenticate(HttpServletRequest req){
        String login = req.getParameter("login");
        if(login.length()>100) throw new ApplicationException(ErrorMessage.LONG_LOGIN);String password = req.getParameter("password");
        if(password.length()>100) throw new ApplicationException(ErrorMessage.LONG_PASSWORD);
        return new UserDTO(login,password);
    }

    public static Integer deletedLocationId(HttpServletRequest req){
        try{
            return Integer.parseInt(req.getParameter("databaseId"));
        }catch (NumberFormatException e){
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }

    public static String locationName(HttpServletRequest req){
        return req.getParameter("name");
    }

    public static BigDecimal getLatitude(HttpServletRequest req){
        return new BigDecimal(req.getParameter("lat"));
    }

    public static BigDecimal getLongitude(HttpServletRequest req){
        return new BigDecimal(req.getParameter("lon"));
    }
}
