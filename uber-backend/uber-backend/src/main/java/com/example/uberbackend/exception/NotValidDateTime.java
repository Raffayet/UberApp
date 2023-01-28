package com.example.uberbackend.exception;

import org.aspectj.weaver.ast.Not;

public class NotValidDateTime extends RuntimeException {

    public NotValidDateTime(){}

    public NotValidDateTime(String not_valid_date_time) {
        super(not_valid_date_time);
    }
}
