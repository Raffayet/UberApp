package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotEnoughTokensException extends RuntimeException{
    public NotEnoughTokensException(String message) {
        super(message);
    }
}
