package com.example.uberbackend.service;

import com.example.uberbackend.exception.CustomValidationException;
import com.example.uberbackend.repositories.VehicleTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class VehicleTypeService {
    private final VehicleTypeRepository vehicleTypeRepository;

    public List<String> getVehicleTypes() {
        Optional<List<String>> vehicleTypesopt = vehicleTypeRepository.getAllVehicleTypes();
        if(vehicleTypesopt.isEmpty()){
            throw new EmptyStackException();
        }
        return vehicleTypesopt.get();

    }
}
