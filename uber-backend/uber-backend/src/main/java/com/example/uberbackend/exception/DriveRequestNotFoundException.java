package com.example.uberbackend.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DriveRequestNotFoundException extends RuntimeException{

    public DriveRequestNotFoundException() {
        super("Drive request has not been found!");
    }
}
