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
public class DriveAssignatureDto {
    @NotNull(message = "Request Id can't be null!")
    private Long requestId;
    @NotNull(message = "Driver email can't be null!")
    @Email(message = "Wrong email format")
    private String driverEmail;
    @NotNull(message = "Inititator email can't be null!")
    @Email(message = "Wrong email format")
    private String initiatorEmail;
}
