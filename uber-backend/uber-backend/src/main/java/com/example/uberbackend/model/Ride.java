package com.example.uberbackend.model;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.dto.MapSearchResultDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private Driver driver;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Client> clients;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<MapSearchResultDto> locations;

    public Ride(DriveRequest driveRequest, Driver driver) {
        this.driver = driver;
        this.clients = driveRequest.getPeople();
        this.initiator = driveRequest.getInitiator();
        this.price = driveRequest.getPrice();
        this.pricePerPassenger = driveRequest.getPricePerPassenger();
        this.vehicleType = driveRequest.getVehicleType();
        this.routeType = driveRequest.getRouteType();
        this.locations = driveRequest.getLocations();
    }
}
