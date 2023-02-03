package com.example.uberbackend.dto;
import com.example.uberbackend.model.enums.RideInviteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriveInvitationDto {
    @NotBlank(message = "First Name is mandatory")
    @Email(message = "Wrong email format")
    private String emailFrom;
    @NotEmpty(message = "Email to list can't be null")
    private List<String> emailsTo;
    @NotBlank(message = "First Location can't be empty")
    private String firstLocation;
    @NotBlank(message = "Destination can't be empty")
    private String destination;
    @NotNull
    private RideInviteStatus rideInviteStatus;
    @Min(value = 0, message = "Price to pay must be positive number")
    private double priceToPay;
}
