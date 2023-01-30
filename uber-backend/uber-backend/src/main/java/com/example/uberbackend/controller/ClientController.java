package com.example.uberbackend.controller;

import com.example.uberbackend.dto.CheckForEnoughTokens;
import com.example.uberbackend.dto.DriveInvitationDto;
import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.dto.InvitationStatusDto;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.dto.FavoriteRouteDto;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.service.ClientService;
import com.example.uberbackend.task.NotificationScheduler;
import com.example.uberbackend.task.ReservationScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/client")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ClientController {

    private ClientService clientService;

    private ThreadPoolTaskScheduler taskScheduler;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ClientController(ClientService clientService, ThreadPoolTaskScheduler taskScheduler, SimpMessagingTemplate simpMessagingTemplate){
        this.clientService = clientService;
        this.taskScheduler = taskScheduler;
        this.simpMessagingTemplate = simpMessagingTemplate;
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
    public ResponseEntity<?> getRideInvites( @RequestParam("email")  String userEmail){
        List<RideInvite> rideInvites = clientService.findAllRideInvites(userEmail);
        return ResponseEntity.ok(rideInvites);
    }

    @PostMapping("create-drive-invitation")
    public ResponseEntity<?> createDriveInvitation(@RequestBody @Valid DriveInvitationDto driveInvitationDTO, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        this.clientService.createDriveInvitation(driveInvitationDTO);
        for (String email :driveInvitationDTO.getEmailsTo()) {
            this.simpMessagingTemplate.convertAndSendToUser(email, "/ride-invites", driveInvitationDTO);
        }
        return ResponseEntity.ok(driveInvitationDTO);
    }

    @PutMapping("change-drive-invitation-status")
    public ResponseEntity<?> changeDriveInvitationStatus(@RequestBody @Valid InvitationStatusDto invitationStatusDto, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        this.clientService.changeDriveInvitationStatus(invitationStatusDto);

        return ResponseEntity.ok(invitationStatusDto);
    }

    @PostMapping("create-drive-request")
    public ResponseEntity<?> createDriveRequest(@RequestBody @Valid DriveRequestDto driveRequestDto, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        this.clientService.createDriveRequest(driveRequestDto);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("create-reservation-drive-request")
    public ResponseEntity<?> createReservationDriveRequest(@RequestBody DriveRequestDto driveRequestDto){
        try{
            LocalDateTime scheduledFor = driveRequestDto.getTimeOfReservation();
            taskScheduler.schedule(new ReservationScheduler(this.clientService, driveRequestDto), scheduledFor.toInstant(ZoneOffset.UTC));

            taskScheduler.schedule(new NotificationScheduler(driveRequestDto, simpMessagingTemplate, 15), scheduledFor.minusMinutes(15).toInstant(ZoneOffset.UTC));
            taskScheduler.schedule(new NotificationScheduler(driveRequestDto, simpMessagingTemplate, 10), scheduledFor.minusMinutes(10).toInstant(ZoneOffset.UTC));
            taskScheduler.schedule(new NotificationScheduler(driveRequestDto, simpMessagingTemplate, 5), scheduledFor.minusMinutes(5).toInstant(ZoneOffset.UTC));

            return ResponseEntity.ok("Success!");
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("invited-has-money")
    public ResponseEntity<?> invitedHasTokens(@RequestBody @Valid CheckForEnoughTokens checkForEnoughTokens, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }

        String hasEnoughTokens = this.clientService.invitedHasTokens(checkForEnoughTokens);
        return ResponseEntity.ok(hasEnoughTokens);
    }

    @PostMapping("refund-tokens")
    public ResponseEntity<?> refundTokens(@RequestBody Long requestId)
    {
        this.clientService.refundTokens(requestId);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("refund-tokens-after-accepting")
    public ResponseEntity<?> refundTokensAfterAccepting(@RequestBody Long requestId)
    {
        this.clientService.refundTokensAfterAccepting(requestId);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("add-favorite-route")
    public ResponseEntity<Boolean> addFavoriteRoute(@RequestBody FavoriteRouteDto favoriteRouteDto)
    {
        try{
            boolean success = this.clientService.addFavoriteRoute(favoriteRouteDto);
            return ResponseEntity.ok(success);
        }catch (Exception ex){
            return new ResponseEntity<Boolean>(Boolean.valueOf(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
