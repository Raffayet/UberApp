package com.example.uberbackend.controller;

import com.example.uberbackend.service.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/vehicleType")
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    @GetMapping("/")
    public ResponseEntity<?> getVehicleTypes(){
        try{
            ArrayList<String> vehicleTypes = (ArrayList<String>) vehicleTypeService.getVehicleTypes();
            return ResponseEntity.ok(vehicleTypes);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
