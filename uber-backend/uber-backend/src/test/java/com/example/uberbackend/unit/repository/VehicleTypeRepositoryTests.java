package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.User;
import com.example.uberbackend.model.VehicleType;
import com.example.uberbackend.repositories.RoleRepository;
import com.example.uberbackend.repositories.UserRepository;
import com.example.uberbackend.repositories.VehicleTypeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class VehicleTypeRepositoryTests {

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;


    @Test
    void getAllVehicleTypesSuccessTest()
    {
        Optional<List<String>> allVehicleTypes = vehicleTypeRepository.getAllVehicleTypes();

        assertTrue(allVehicleTypes.isPresent());
        assertEquals(4, allVehicleTypes.get().size());

        assertThat( allVehicleTypes.get(), contains(
                "Standard", "Baby Seat", "Pet Friendly", "Baby Seat and Pet Friendly"
        ));
    }

    @Test
    void findByTypeSuccessTest()
    {
        Optional<VehicleType> vehicleType = vehicleTypeRepository.findByType("Standard");

        assertTrue(vehicleType.isPresent());
        assertEquals("Standard", vehicleType.get().getType());
    }

    @Test
    void findByTypeNoSuchTypeTest()
    {
        Optional<VehicleType> vehicleType = vehicleTypeRepository.findByType("None");

        assertTrue(vehicleType.isEmpty());
    }

}
