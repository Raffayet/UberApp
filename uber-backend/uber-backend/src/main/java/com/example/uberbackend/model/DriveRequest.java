package com.example.uberbackend.model;

import com.example.uberbackend.dto.MapSearchResultDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DriveRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Boolean isReserved;
    private LocalDate reserveDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client initiator;

    private double price;
    private double pricePerPassenger;
    private String vehicleType;
    private String routeType;

    @ManyToMany
    private List<Client> people;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<MapSearchResultDto> locations;

    @ManyToMany
    List<Driver> driversThatRejected;

}
