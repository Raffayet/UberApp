package com.example.uberbackend.exception;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {

    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }
}

