package com.example.uberbackend.controller;

import com.example.uberbackend.dto.CheckForEnoughTokens;
import com.example.uberbackend.dto.DriveInvitationDto;
import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.service.ClientService;
import com.example.uberbackend.task.ReservationScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;

    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    public ClientController(ClientService clientService, ThreadPoolTaskScheduler taskScheduler){
        this.clientService = clientService;
        this.taskScheduler = taskScheduler;
    }

    @GetMapping("get-tokens")
    public ResponseEntity<?> getAmountOfTokens(@RequestParam String email){
        try{
            double amount = clientService.getTokensByEmail(email);
            return ResponseEntity.ok(amount);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add-tokens")
    public ResponseEntity<?> addTokens(@RequestBody String email){
        try{
            double amount = clientService.getTokensByEmail(email);
            return ResponseEntity.ok(amount);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-ride-invites")
    public ResponseEntity<?> getRideInvites(@RequestParam("email") String userEmail){
        List<RideInvite> rideInvites;
        try{
            rideInvites = clientService.findAllRideInvites(userEmail);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(rideInvites);
    }

    @PostMapping("create-drive-invitation")
    public ResponseEntity<?> createDriveInvitation(@RequestBody DriveInvitationDto driveInvitationDTO){
        try{
            this.clientService.createDriveInvitation(driveInvitationDTO);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(driveInvitationDTO);
    }

    @PutMapping("change-drive-invitation-status")
    public ResponseEntity<?> changeDriveInvitationStatus(@RequestBody HashMap<String, String> DTO){
        try{
            this.clientService.changeDriveInvitationStatus(DTO);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTO);
    }

    @PostMapping("create-drive-request")
    public ResponseEntity<?> createDriveRequest(@RequestBody DriveRequestDto driveRequestDto){
        try{
            this.clientService.createDriveRequest(driveRequestDto);
            return ResponseEntity.ok("Success!");
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("create-reservation-drive-request")
    public ResponseEntity<?> createReservationDriveRequest(@RequestBody DriveRequestDto driveRequestDto){
        try{
            Date scheduledFor = driveRequestDto.getTimeOfReservation();
            taskScheduler.schedule(new ReservationScheduler(this.clientService, driveRequestDto), scheduledFor);
            return ResponseEntity.ok("Success!");
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("invited-has-money")
    public ResponseEntity<?> invitedHasTokens(@RequestBody CheckForEnoughTokens checkForEnoughTokens){
        try{
            boolean hasEnoughTokens = this.clientService.invitedHasTokens(checkForEnoughTokens);
            return ResponseEntity.ok(hasEnoughTokens);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
