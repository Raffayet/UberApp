package com.example.uberbackend.controller;

import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.dto.UserDrivingStatus;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.model.User;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/driver")
@RequiredArgsConstructor
public class DriverController {

    private final UserService userService;
    private final DriverService driverService;
    private final JwtTokenGenerator tokenGenerator;

    @PutMapping(value = "/update-personal-info")
    public ResponseEntity<?> updatePersonalInfo(@RequestBody PersonalInfoUpdateDto dto){
        try{
            driverService.updatePersonalInfo(dto);
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
}
