package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoAvailableDriversException extends RuntimeException{

    public NoAvailableDriversException(String message) {
        super(message);
    }
}
