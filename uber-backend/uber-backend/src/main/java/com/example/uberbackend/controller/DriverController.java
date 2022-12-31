package com.example.uberbackend.controller;

import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import com.example.uberbackend.dto.RegisterDriverDto;
import com.example.uberbackend.dto.RegisterDto;
import com.example.uberbackend.dto.UserDrivingStatus;
import com.example.uberbackend.model.User;
import com.example.uberbackend.security.JwtTokenGenerator;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
