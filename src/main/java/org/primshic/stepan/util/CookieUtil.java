package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {

    public static String getSessionIdByCookie(Cookie[] cookies){
        if(cookies == null || cookies.length==0) return null;
        Cookie uuidCookie;
        for(Cookie cookie : cookies){
            if(Objects.equals(cookie.getName(), "uuid")) {
                uuidCookie = cookie;
                return uuidCookie.getValue();
            }
        }
        return null;
    }
}
