package com.example.uberbackend.validator;

import com.example.uberbackend.dto.RegisterDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class PasswordMatchValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDto user = (RegisterDto) target;

        if (!user.getPassword().equals(user.getConfirmPassword()))
            errors.rejectValue("confirmPassword","match", "Passwords must match.");
    }
}
