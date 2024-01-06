package org.primshic.stepan.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static void handle(HttpServletResponse response, ApplicationException e) {
        response.setStatus(e.getError().getStatus());
        response.setContentType("application/json;charset=UTF-8");
        ErrorDetails errorDetails = new ErrorDetails(e.getError().getStatus(), e.getMessage());
        try {
            mapper.writeValue(response.getOutputStream(),errorDetails);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
