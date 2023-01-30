package com.example.uberbackend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverRejectionDto {

    @NotNull(message = "Request Id can't be null!")
    private Long requestId;
    @NotNull(message = "Driver email can't be null!")
    @Email(message = "Driver email is not of a correct type")
    private String driverEmail;
    @NotNull(message = "Initiator email can't be null!")
    @Email(message = "Initiator email is not of a correct type")
    private String initiatorEmail;
    @NotNull(message = "Reason for rejection can't be null!")
    private String reasonForRejection;
}
