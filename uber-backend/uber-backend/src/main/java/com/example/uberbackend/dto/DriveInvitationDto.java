package com.example.uberbackend.dto;
import com.example.uberbackend.model.enums.RideInviteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriveInvitationDto {
    private String emailFrom;
    private List<String> emailsTo;
    private String firstLocation;
    private String destination;
    private RideInviteStatus rideInviteStatus;
    private double priceToPay;
}
