package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class RideInviteNotFoundException extends RuntimeException{
    public RideInviteNotFoundException() {
        super("Ride invite has not been found!");
    }
}
