package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {
    @NotBlank(message = "Model is mandatory")
    private String model;
    @NotBlank(message = "Vehicle Type is mandatory")
    private String vehicleType;
}
