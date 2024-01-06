package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.primshic.stepan.dto.location_weather.LocationWeatherDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.Session;
import org.primshic.stepan.model.User;
import org.primshic.stepan.services.SessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionUtil {
    private static Logger log = Logger.getLogger(SessionUtil.class.getName());

    private static SessionService sessionService = new SessionService();
    public static Optional<Session> getCurrentSession(String sessionId) {
        Optional<Session> userSession = Optional.empty();
        if (validSessionId(sessionId)) {
            log.info("Session ID is valid: {}" + sessionId);
            userSession = sessionService.getById(sessionId);
        }
        return userSession;
    }

    public static void deleteSessionIfPresent(String sessionId){
        if (validSessionId(sessionId)) {
            sessionService.getById(sessionId).ifPresent(sessionService::delete);
        }
    }

    public static boolean validSessionId(String sessionId){
        return sessionId != null && !sessionId.isEmpty();
    }
}
