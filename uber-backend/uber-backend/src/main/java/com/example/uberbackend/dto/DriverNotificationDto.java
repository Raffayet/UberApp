package com.example.uberbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverNotificationDto {
    private Long requestId;
    private MapSearchResultDto firstLocation;
}
