package com.example.uberbackend.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @Email(message = "Wrong email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
            message = "Password must contain at least one upper letter, at least one lower latter and at least one digit")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Telephone is mandatory")
    private String telephone;

    @Pattern(regexp = "^(LOCAL|GOOGLE|FACEBOOK)$", message = "Provider must be one of following LOCAL,GOOGLE or FACEBOOK")
    @NotBlank(message = "Provider is mandatory")
    private String provider;
}
