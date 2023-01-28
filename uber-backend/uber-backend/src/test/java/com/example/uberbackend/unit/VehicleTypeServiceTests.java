package com.example.uberbackend.unit;

import com.example.uberbackend.exception.NoVehicleTypesException;
import com.example.uberbackend.exception.PaymentFailedException;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.VehicleType;
import com.example.uberbackend.repositories.DriverRepository;
import com.example.uberbackend.repositories.VehicleTypeRepository;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.VehicleTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class VehicleTypeServiceTests {

    @InjectMocks
    VehicleTypeService vehicleTypeService;

    @Mock
    VehicleTypeRepository vehicleTypeRepository;

    @Test
    void getVehicleTypesSuccessTest(){

        List<String> vehicleTypeList = List.of("Standard", "Baby Seat");

        Mockito.when(vehicleTypeRepository.getAllVehicleTypes()).thenReturn(Optional.of(vehicleTypeList));

        assertEquals(vehicleTypeService.getVehicleTypes(), vehicleTypeList);
    }

    @Test
    void getVehicleTypesNoVehicleTypesExTest(){
        Mockito.when(vehicleTypeRepository.getAllVehicleTypes()).thenReturn(Optional.empty());
        assertThrows(NoVehicleTypesException.class,()->vehicleTypeService.getVehicleTypes());
    }
}
