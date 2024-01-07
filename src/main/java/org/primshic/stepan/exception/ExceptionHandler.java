package org.primshic.stepan.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void handle(HttpServletResponse response, ApplicationException e) {
        log.error("Handling exception: {}", e.getMessage(), e);

        response.setStatus(e.getError().getStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(mapper.writeValueAsString(e.getError().getMessage()));
        } catch (IOException ex) {
            log.error("Error writing response: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
}
