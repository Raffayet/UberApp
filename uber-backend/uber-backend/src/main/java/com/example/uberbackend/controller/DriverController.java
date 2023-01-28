package com.example.uberbackend.controller;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.*;
import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.dto.RegisterDriverDto;
import com.example.uberbackend.dto.RegisterDto;
import com.example.uberbackend.dto.UserDrivingStatus;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/driver")
@RequiredArgsConstructor
public class DriverController {

    private final UserService userService;
    private final DriverService driverService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtTokenGenerator tokenGenerator;

    @PutMapping(value = "/update-personal-info")
    public ResponseEntity<?> updatePersonalInfo(@RequestBody PersonalInfoUpdateDto dto){
        try{
            driverService.updatePersonalInfo(dto);
            simpMessagingTemplate.convertAndSend("/info-changed-request", "ok");
            return ResponseEntity.ok("Your changes have been sent on approval to admin!");
        }catch (Exception ex){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }
//    @GetMapping(value = "/get-rides-to-take")
//    public ResponseEntity<?> getRidesToTake(){
//        List<RideInvite> rideInvites;
//        try{
//            driveRequests = driverService.findAllDriveReqeusts();
//        }catch(Exception ex){
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//        return ResponseEntity.ok(rideInvites);
//    }

    @PostMapping("driver-logout")
    public ResponseEntity<?> logout(@RequestBody String driverEmail){
        try{
            this.driverService.resetAfterLogout(driverEmail);
            return ResponseEntity.ok("Success!");
        }catch (Exception ex){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("assign-drive-to-driver")
    public ResponseEntity<?> assignDriveToDriver(@RequestBody DriveAssignatureDto driveAssignatureDto){
        try{
            this.driverService.assignDriveToDriver(driveAssignatureDto);
            return ResponseEntity.ok("Success!");
        }catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> registerDriver(@Valid @RequestBody RegisterDriverDto registerDriverDto, BindingResult result){
        try{
            String message = userService.registerDriver(registerDriverDto, result);
            if (message.equals("Success"))
                return new ResponseEntity<>(HttpStatus.CREATED);
            return ResponseEntity.badRequest().build();
        }catch (RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("reject-drive")
    public ResponseEntity<?> rejectDrive(@RequestBody @Valid DriverRejectionDto driverRejectionDto, BindingResult result){
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        this.driverService.rejectDrive(driverRejectionDto);
        return ResponseEntity.ok("Success!");
    }

    @PostMapping("reject-drive-after-accepting")
    public ResponseEntity<?> rejectDriveAfterAccepting(@RequestBody DriverRejectionDto driverRejectionDto){
        try{
            this.driverService.rejectDriveAfterAccepting(driverRejectionDto);
            return ResponseEntity.ok("Success!");
        }catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-rides")
    public ResponseEntity<?> getRides(@RequestParam("driverEmail") String driverEmail){
        List<RideToShowDto> ridesToShowDto;
        try{
            ridesToShowDto = this.driverService.findAllRidesToDo(driverEmail);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(ridesToShowDto);
    }

    @GetMapping("get-driver-info")
    public ResponseEntity<?> getDriverInfo(@RequestParam("rideId") Long rideId) {
        DriverInfoDto driverInfoDto;
        try {
            driverInfoDto = this.driverService.getDriverInfoByRideId(rideId);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(driverInfoDto);
    }


    @GetMapping("get-all-active")
    public ResponseEntity<?> getActiveDrivers(){
        try {
            List<MapDriverDto> mapDriverDtos = driverService.getActiveDrivers();
            return ResponseEntity.ok(mapDriverDtos);
        }
        catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("rate-driver")
    public ResponseEntity<?> rateDriver(@RequestBody RateDriverDto rateDriverDto){
        try{
            this.driverService.rateDriver(rateDriverDto);
            return ResponseEntity.ok("Success!");
        }catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-already-rated-rides")
    public ResponseEntity<?> getAlreadyRatedRides(@RequestParam("clientEmail") String clientEmail){
        List<Long> alreadyRatedRideIds;
        try{
            List<Driver> allRatedDrivers = this.driverService.getAllRatedDrivers(clientEmail);
            alreadyRatedRideIds = this.driverService.findRideIdsByDriver(allRatedDrivers);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(alreadyRatedRideIds);
    }

    @GetMapping("get-email-by-id")
    public ResponseEntity<String> getEmailById(@RequestParam("driverId") Long driverId){
        String driverEmail = "";
        try{
            driverEmail = this.driverService.getDriverEmailById(driverId);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(driverEmail);
    }

    @PostMapping("set-rating-expiration")
    public ResponseEntity<?> setRatingExpiration(@RequestBody Long rideId){
        try{
            this.driverService.setRatingExpiration(rideId);
            return ResponseEntity.ok("Success!");
        }catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-info-requests")
    public ResponseEntity<?> getDriverInfoChangeRequests(){
        try{
            List<DriverInfoChangeRequest> driverRequests = this.driverService.getDriverInfoChangeRequests();
            return ResponseEntity.ok(driverRequests);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("respond-to-info-request")
    public ResponseEntity<?> respondToInfoRequest(@RequestBody DriverInfoChangeRequest dto){
        try{
            DriverInfoChangeResponse driverInfoChangeResponse = this.driverService.respondToInfoRequest(dto);
            simpMessagingTemplate.convertAndSendToUser(dto.getOldData().getEmail(),"/info-changed-request", driverInfoChangeResponse);
            return ResponseEntity.ok(driverInfoChangeResponse);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
