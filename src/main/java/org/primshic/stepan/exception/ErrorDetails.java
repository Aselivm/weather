package org.primshic.stepan.exception;


import lombok.*;


@Getter
@Setter
public class ErrorDetails {
    public ErrorDetails(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private int status;
    private String message;
}
