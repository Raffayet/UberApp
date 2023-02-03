package com.example.uberbackend.dto;

import com.example.uberbackend.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriveRequestDto {
    @NotBlank(message = "First Name is mandatory")
    @Email(message = "Wrong email format")
    private String initiatorEmail;

    @Min(value = 0,message = "Price can't be negative")
    private double price;
    @Min(value = 0,message = "Price can't be negative")
    private double pricePerPassenger;
    @Pattern(regexp = "^(Standard|Baby Seat|Pet Friendly|Baby Seat and Pet Friendly)$", message = "Provider must be one of following Standard,Baby Seat,Pet Friendly,Baby Seat and Pet Friendly")
    @NotBlank(message = "Vehicle Type is mandatory")
    private String vehicleType;

    @Pattern(regexp = "^(Custom|Alternative|Optimal)$", message = "Provider must be one of following Standard,Baby Seat,Pet Friendly,Baby Seat and Pet Friendly")
    @NotBlank(message = "Route Type is mandatory")
    private String routeType;
    private List<String> people;
    @NotEmpty(message = "Name may not be empty")
    @Size(min = 2, message = "There must be at least 2 locations")
    private List<MapSearchResultDto> locations;
    private Boolean isReserved;
    private LocalDateTime timeOfReservation;
    private LocalDateTime timeOfRequestForReservation;
}
