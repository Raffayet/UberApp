package com.example.uberbackend.exception;

import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotValidDateTime extends RuntimeException {

    public NotValidDateTime(){}

    public NotValidDateTime(String not_valid_date_time) {
        super(not_valid_date_time);
    }
}
