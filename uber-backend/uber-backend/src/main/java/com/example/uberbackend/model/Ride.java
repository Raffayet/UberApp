package com.example.uberbackend.model;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.dto.MapSearchResultDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Driver driver;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Client> clients;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private Client initiator;

    @JoinColumn(name = "price")
    private double price;

    @JoinColumn(name = "price_per_passenger")
    private double pricePerPassenger;

    @JoinColumn(name = "vehicle_type")
    private String vehicleType;

    @JoinColumn(name = "route_type")
    private String routeType;

    @JoinColumn(name = "reserved")
    private Boolean reserved;

    private LocalDateTime timeOfReservation;
    private LocalDateTime timeOfRequestForReservation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<MapSearchResultDto> locations;

    private Boolean ratingExpired;

    public Ride(DriveRequest driveRequest, Driver driver) {
        this.driver = driver;
        this.clients = setClientsForRide(driveRequest.getPeople());
        this.initiator = driveRequest.getInitiator();
        this.price = driveRequest.getPrice();
        this.pricePerPassenger = driveRequest.getPricePerPassenger();
        this.vehicleType = driveRequest.getVehicleType();
        this.routeType = driveRequest.getRouteType();
        this.reserved = driveRequest.getIsReserved();
        this.timeOfReservation = driveRequest.getTimeOfReservation();
        this.timeOfRequestForReservation = driveRequest.getTimeOfRequestForReservation();
        this.locations = setLocationsForRide(driveRequest.getLocations());
        this.startTime = driveRequest.getTimeOfReservation();
        this.endTime = driveRequest.getTimeOfReservation();
    }

    private List<MapSearchResultDto> setLocationsForRide(List<MapSearchResultDto> locations) {
        return new ArrayList<MapSearchResultDto>(locations);
    }

    private List<Client> setClientsForRide(List<Client> people)
    {
        return new ArrayList<Client>(people);
    }

}
