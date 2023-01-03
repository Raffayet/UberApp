package com.example.uberbackend.dto;

import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapRideDto {
    private long id;
    private List<LocationDto> atomicPoints;
    private MapDriverDto driver;
    private RideStatus status;
    private List<String> clientEmails;

    public MapRideDto(Ride ride){
        this.id = ride.getId();
        this.driver = new MapDriverDto(ride.getDriver());
        this.status = ride.getRideStatus();
        this.clientEmails = ride.getClients().stream().map(client -> client.getEmail()).collect(Collectors.toList());
    }
}
