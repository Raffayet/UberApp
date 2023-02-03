package com.example.uberbackend.model;

import com.example.uberbackend.model.enums.RideInviteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailFrom;

    private String emailTo;

    private String firstLocation;

    private String destination;

    private double priceToPay;

    private RideInviteStatus rideInviteStatus;
}
