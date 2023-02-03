package com.example.uberbackend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginDto {

    @Email(message = "Wrong email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Id Token is mandatory")
    private String idToken;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @NotBlank(message = "Photo Url is mandatory")
    private String photoUrl;

    @Pattern(regexp = "^(LOCAL|GOOGLE|FACEBOOK)$", message = "Provider must be one of following LOCAL,GOOGLE or FACEBOOK")
    @NotBlank(message = "Provider is mandatory")
    private String provider;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Telephone is mandatory")
    private String telephone;
}
