package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotEnoughLocationsException extends RuntimeException {
    public NotEnoughLocationsException(){super();}


    public NotEnoughLocationsException(String message){super(message);}

}
