package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private ChartDataDto ridesPerDay;
    private ChartDataDto kmPerDay;
    private ChartDataDto moneyPerDay;
    private ReportResponseDto adminReport;
}
