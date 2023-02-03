package com.example.uberbackend.model;

import com.example.uberbackend.model.enums.AccountStatus;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.Provider;
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

    @OneToMany(fetch = FetchType.LAZY)
    private List<FavoriteRoute> favoriteRoutes;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Ride> takenRides;

    private double tokens = 0;
}
