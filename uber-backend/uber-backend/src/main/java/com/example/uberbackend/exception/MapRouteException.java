package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class MapRouteException extends RuntimeException {

    public MapRouteException(){
        super();
    }

    public MapRouteException(String message){
        super(message);
    }

}
