package com.example.uberbackend.dto;

import com.example.uberbackend.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideToShowDto {
    private Long id;
    private String initiator;
    private double price;
    private String firstLocation;
    private String destination;

    public RideToShowDto(Ride ride) {
        this.id = ride.getId();
        this.initiator = ride.getInitiator().getEmail();
        this.price = ride.getPrice();
        setRideToShowLocation(ride);
    }

    private void setRideToShowLocation(Ride ride) {
        String firstLocation = ride.getLocations().get(0).getDisplayName();
        String destination = ride.getLocations().get(ride.getLocations().size() - 1).getDisplayName();
        this.firstLocation = firstLocation;
        this.destination = destination;
    }
}
