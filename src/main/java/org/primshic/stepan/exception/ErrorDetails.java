package org.primshic.stepan.exception;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDetails {
    public ErrorDetails(int status, String message) {
        this.status = status;
        this.message = message;
        timestamp = LocalDateTime.now();
    }

    private int status;
    private String message;
    private LocalDateTime timestamp;
}
