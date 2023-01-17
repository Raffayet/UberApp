package com.example.uberbackend.controller;

import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.dto.DriveInvitationDto;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final DriverService driverService;

    @GetMapping("get-all")
    public Page<Ride> getEndedRides(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam String email) {
        Pageable paging = PageRequest.of(page, size);
        return rideService.findEndedRidesByEmail(paging, email);
    }

    @MessageMapping("/ride-invite")
    public void receiveRideInvite(@Payload DriveInvitationDto dto){
        for(String email : dto.getEmailsTo()){
            simpMessagingTemplate.convertAndSendToUser(email, "/ride-invites", dto);
        }
    }

    @MessageMapping("/ride-response")
    public void responseToRideInvite(@Payload HashMap<String, String> dto){
        simpMessagingTemplate.convertAndSendToUser(dto.get("email"), "/response-ride-invites", dto);
    }

    @GetMapping("/active-driver")
    public ResponseEntity<MapDriverDto> getActiveDriver() {
        //zakucano
        Driver driver = driverService.getDriver(2L);
        MapDriverDto mapDriverDto = new MapDriverDto(driver);
        return new ResponseEntity<>(mapDriverDto, HttpStatus.OK);
    }

    @GetMapping(path="/active-rides", produces = "application/json")
    public ResponseEntity<?> getActiveRides(){
        List<MapRideDto> rides = rideService.getActiveRides();
        return new ResponseEntity<>(rides, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<?> changeRide(@PathVariable("id") long id) {
        Ride ride = this.rideService.changeRide(id);
        MapRideDto returnRideDTO = new MapRideDto(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping("calculate-price")
    public ResponseEntity<?> calculatePrice(@RequestParam("vehicleType") String vehicleType, @RequestParam("totalDistance") double totalDistance) {
        try {
            double calculatedPrice = this.rideService.calculatePrice(vehicleType, totalDistance);
            return ResponseEntity.ok(calculatedPrice);
        }
        catch (RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
