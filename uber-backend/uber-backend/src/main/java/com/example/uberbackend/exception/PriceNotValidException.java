package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PriceNotValidException extends RuntimeException {
    public PriceNotValidException() {}

    public PriceNotValidException(String message) {
        super(message);
    }
}
