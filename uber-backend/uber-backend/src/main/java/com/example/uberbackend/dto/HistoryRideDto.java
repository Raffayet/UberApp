package com.example.uberbackend.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRideDto {
    private Long id;
    private String firstLocation;
    private String destination;
    private double price;
    private String startDate;
    private String endDate;
}
