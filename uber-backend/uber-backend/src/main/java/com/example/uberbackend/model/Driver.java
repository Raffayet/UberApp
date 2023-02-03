package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {
    private Double dailyActiveInterval;
    private LocalDateTime lastTimeOfLogin;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Ride> rides;

    @OneToMany
    private List<Rating> ratingsFromClients;

    @OneToOne
    private Point currentLocation;
}
