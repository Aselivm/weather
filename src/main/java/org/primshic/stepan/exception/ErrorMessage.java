package org.primshic.stepan.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    INTERNAL_ERROR("Internal error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR),

    WRONG_PASSWORD("Wrong password",HttpServletResponse.SC_UNAUTHORIZED),
    LOGIN_NOT_EXIST("Such login does not exist",HttpServletResponse.SC_UNAUTHORIZED),

    LOGIN_ALREADY_EXIST("Login already exist",HttpServletResponse.SC_BAD_REQUEST),
    LONG_LOGIN("Login length exceeds 100 characters", HttpServletResponse.SC_BAD_REQUEST),
    LONG_PASSWORD("Password length exceeds 60 characters", HttpServletResponse.SC_BAD_REQUEST);


    private final String message;
    private final int status;
}