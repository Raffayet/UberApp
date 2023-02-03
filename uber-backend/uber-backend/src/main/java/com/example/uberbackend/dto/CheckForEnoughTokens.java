package com.example.uberbackend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckForEnoughTokens {
    @NotNull(message = "First Name is mandatory")
    @Email(message = "Wrong email format")
    private String initiatorEmail;
    @NotNull
    private String[] peopleEmails;
    @NotNull
    @PositiveOrZero(message = "Price can't be negative")
    private double pricePerPassenger;
}
