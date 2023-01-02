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
public class Client extends User {
    @OneToMany
    private List<DriveRequest> driveRequests;

    @OneToMany
    private List<Route> favoriteRoutes;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Ride> takenRides;

    private double tokens = 0;
}
