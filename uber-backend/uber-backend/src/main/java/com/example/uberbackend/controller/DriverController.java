package com.example.uberbackend.controller;

<<<<<<< HEAD
import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.RideInvite;
=======
import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.dto.RegisterDriverDto;
import com.example.uberbackend.dto.RegisterDto;
import com.example.uberbackend.dto.UserDrivingStatus;
>>>>>>> develop
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
=======
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
>>>>>>> develop

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

<<<<<<< HEAD
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
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
    }

=======
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
>>>>>>> develop
}
