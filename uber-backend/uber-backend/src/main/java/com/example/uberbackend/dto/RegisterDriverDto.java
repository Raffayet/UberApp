package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDriverDto extends RegisterDto{
    @NotNull(message = "Vehicle cannot be empty")
    private VehicleDto vehicle;

    RegisterDriverDto(){
        this.setProvider("LOCAL");
    }

}
