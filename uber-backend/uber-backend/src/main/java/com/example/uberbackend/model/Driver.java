package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {
    private Double currentActiveInterval;
    private Double dailyActiveInterval;
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToMany
    private List<DriveRequest> driveRequests;

    @OneToMany
    private List<Rating> ratingsFromClients;

}
