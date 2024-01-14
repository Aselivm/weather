package org.primshic.stepan.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    INTERNAL_ERROR("Internal error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR),

    DATABASE_ERROR("Database error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR),

    WRONG_PASSWORD("Wrong password", HttpServletResponse.SC_UNAUTHORIZED),
    LOGIN_NOT_EXIST("Such login does not exist", HttpServletResponse.SC_UNAUTHORIZED),

    BAD_REQUEST("Bad request",HttpServletResponse.SC_BAD_REQUEST),

    EMPTY_INPUT("The search field must not be empty", HttpServletResponse.SC_BAD_REQUEST),

    LOGIN_ALREADY_EXISTS("Login already exists", HttpServletResponse.SC_BAD_REQUEST),
    LONG_LOGIN("Login length exceeds 100 characters", HttpServletResponse.SC_BAD_REQUEST),
    LONG_PASSWORD("Password length exceeds 60 characters", HttpServletResponse.SC_BAD_REQUEST),
    LOGIN_CONTAINS_SPACES("Login must not have spaces.", HttpServletResponse.SC_BAD_REQUEST),
    PASSWORD_CONTAINS_SPACES("Password must not have spaces.", HttpServletResponse.SC_BAD_REQUEST),
    OPEN_WEATHER_ERROR("OpenWeatherAPI Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


    private final String message;
    private final int status;
}
