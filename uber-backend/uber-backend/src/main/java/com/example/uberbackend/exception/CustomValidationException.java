package com.example.uberbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class CustomValidationException extends RuntimeException {

    Map<String, String> errors;

    public CustomValidationException(String message) {
        super(message);
    }

    public CustomValidationException(Map<String, String> errors) {
        this(errors, "Validation failed, check your input");
    }

    public CustomValidationException(Map<String, String> errors, String message) {
        this(message);
        this.errors = errors;
    }
}
