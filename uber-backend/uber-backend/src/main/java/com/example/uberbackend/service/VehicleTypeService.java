package com.example.uberbackend.service;

import com.example.uberbackend.exception.CustomValidationException;
import com.example.uberbackend.exception.NoVehicleTypesException;
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
        List<String> vehicleTypes = vehicleTypeRepository.getAllVehicleTypes().orElseThrow(NoVehicleTypesException::new);
        return vehicleTypes;
    }
}
