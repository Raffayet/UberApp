package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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

    private double tokens = 0;
}
