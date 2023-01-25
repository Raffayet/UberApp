package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateDriverDto {
    private int numberOfStars;
    private String comment;
    private String clientEmail;
    private String driverEmail;
}
