package com.example.uberbackend.dto;

import com.example.uberbackend.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriveRequestDto {
    private String initiatorEmail;
    private double price;
    private double pricePerPassenger;
    private String vehicleType;
    private String routeType;
    private List<String> people;
    private List<MapSearchResultDto> locations;
    private Boolean isReserved;
    private Date timeOfReservation;
    private Date timeOfRequestForReservation;
}