package com.example.uberbackend.controller;

import com.example.uberbackend.dto.ReportRequestDto;
import com.example.uberbackend.dto.PathInfoDto;
import com.example.uberbackend.dto.ReportResponseDto;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.MapService;
import com.example.uberbackend.service.ReportService;
import com.example.uberbackend.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DriverService driverService;
    private final RideService rideService;

    @PostMapping
    public ResponseEntity<?> generateReport(@RequestBody ReportRequestDto reportRequestDto){
        try{

            ReportResponseDto reportResponseDto = reportService.generateReports(reportRequestDto);
            return new ResponseEntity<>(reportResponseDto, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }
}
