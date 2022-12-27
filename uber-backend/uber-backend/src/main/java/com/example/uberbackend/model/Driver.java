package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {
    private Double currentActiveInterval;
    private Double dailyActiveInterval;

    @OneToMany
    private List<DriveRequest> driveRequests;

    @OneToMany
    private List<Rating> ratingsFromClients;

    @OneToOne
    private Point currentLocation;
}
