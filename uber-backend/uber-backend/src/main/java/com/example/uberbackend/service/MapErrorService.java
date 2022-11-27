package com.example.uberbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapErrorService {

    public Map<String, String> mapValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors())
                errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;
    }
}
