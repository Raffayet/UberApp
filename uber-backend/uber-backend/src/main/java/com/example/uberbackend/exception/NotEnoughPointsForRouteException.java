package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotEnoughPointsForRouteException extends RuntimeException{
    public NotEnoughPointsForRouteException(String message) {
        super(message);
    }
}
