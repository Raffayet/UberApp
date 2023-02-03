package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class RideNotFoundException extends RuntimeException{
    public RideNotFoundException() {
        super("Ride has not been found!");
    }
}
