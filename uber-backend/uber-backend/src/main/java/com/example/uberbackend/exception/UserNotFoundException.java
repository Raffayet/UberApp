package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User has not been found!");
    }
}
