package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfoDto {
    private String name;
    private String lastName;
    private String email;
    private double averageRating;
}
