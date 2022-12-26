package com.example.uberbackend.controller;

import com.example.uberbackend.dto.DriveInvitationDTO;
import com.example.uberbackend.dto.MessageDto;
import com.example.uberbackend.dto.RegisterDto;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService){
        this.clientService = clientService;
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(rideInvites);
    }

    @PostMapping("create-drive-invitation")
    public ResponseEntity<?> createDriveInvitation(@RequestBody DriveInvitationDTO driveInvitationDTO){
        try{
            this.clientService.createDriveInvitation(driveInvitationDTO);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(driveInvitationDTO);
    }

    @PutMapping("change-drive-invitation-status")
    public ResponseEntity<?> changeDriveInvitationStatus(@RequestBody HashMap<String, String> DTO){
        try{
            this.clientService.changeDriveInvitationStatus(DTO);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(DTO);
    }
}
