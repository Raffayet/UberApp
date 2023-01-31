package com.example.uberbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FavoriteRouteAlreadyExistException extends RuntimeException {

    public FavoriteRouteAlreadyExistException() {
        super("Favorite route has already been added to the list of favorite routes!");
    }
}
