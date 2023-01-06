package com.example.uberbackend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverRejectionDto {
    private Long requestId;
    private String driverEmail;
    private String initiatorEmail;
    private String reasonForRejection;
}
