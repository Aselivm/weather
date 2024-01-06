package org.primshic.stepan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplicationException extends RuntimeException{
    private final ErrorMessage error;
}
