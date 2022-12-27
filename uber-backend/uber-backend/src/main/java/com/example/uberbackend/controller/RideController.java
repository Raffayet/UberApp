package com.example.uberbackend.controller;

import com.example.uberbackend.dto.DriveInvitationDto;
import com.example.uberbackend.model.Ride;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(path = "/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("get-all")
    public Page<Ride> getRides(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable paging = PageRequest.of(page, size);
        return rideService.findAll(paging);
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
