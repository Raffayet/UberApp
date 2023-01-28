package com.example.uberbackend.dto;

import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.enums.RideStatus;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapRideDto {

    @NotNull(message = "Ride id can't be empty!")
    private Long id;
    @NotNull(message = "Information missing!")
    private List<LocationDto> atomicPoints;
    @NotNull(message = "Information missing!")
    private List<LocationDto> atomicPointsBeforeRide;
    @NotNull(message = "Driver can't be empty!")
    private MapDriverDto driver;
    @NotNull(message = "Ride status can't be empty!")
    private RideStatus status;
    @NotNull(message = "Client emails can't be empty!")
    private List<String> clientEmails;
    @NotNull(message = "Duration can't be empty!")
    private int duration;

    public MapRideDto(Ride ride){
        this.id = ride.getId();
        this.driver = new MapDriverDto(ride.getDriver());
        this.status = ride.getRideStatus();
        this.clientEmails = ride.getClients().stream().map(client -> client.getEmail()).collect(Collectors.toList());
        if(!this.clientEmails.contains(ride.getInitiator().getEmail()))
            this.clientEmails.add(ride.getInitiator().getEmail());
    }
}
