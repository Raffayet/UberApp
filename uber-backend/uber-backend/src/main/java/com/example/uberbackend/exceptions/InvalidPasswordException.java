package com.example.uberbackend.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
