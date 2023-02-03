package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class RideCouldNotBeEndedException extends RuntimeException{

    public RideCouldNotBeEndedException() {
        super("Ride needs to be started!");
    }
}
