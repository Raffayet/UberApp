package com.example.uberbackend.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException() {
        super("Driver has not been found!");
    }
}