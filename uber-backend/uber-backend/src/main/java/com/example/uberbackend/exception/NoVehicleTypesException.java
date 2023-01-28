package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoVehicleTypesException extends RuntimeException {

    public NoVehicleTypesException(){
        super("No Vehicle types found");
    }
}
