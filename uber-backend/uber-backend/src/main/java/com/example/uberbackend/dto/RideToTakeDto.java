package com.example.uberbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideToTakeDto {
    private Long requestId;
    private String firstLocation;
    private String destination;
    private String initiatorEmail;
    private Boolean reserved;
    private LocalDateTime drivingTime;
}
