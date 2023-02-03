package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException() {
        super("Client has not been found!");
    }
}
