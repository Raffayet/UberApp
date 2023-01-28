package com.example.uberbackend.exception;

public class NotEnoughLocationsException extends RuntimeException {
    public NotEnoughLocationsException(){super();}


    public NotEnoughLocationsException(String message){super(message);}

}
