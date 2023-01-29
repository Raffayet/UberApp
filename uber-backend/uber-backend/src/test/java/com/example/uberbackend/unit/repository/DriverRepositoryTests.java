package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.repositories.DriverRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

@SpringBootTest
@Transactional
public class DriverRepositoryTests {

    @Autowired
    DriverRepository driverRepository;

    // SW-1-2019
    @Test
    public void whenFindAvailableDrivers_thenReturnListOfTwoElements(){
        List<Driver> drivers = driverRepository.findAvailableDrivers();

        Assertions.assertEquals(2, drivers.size());

        assertThat( drivers, contains(
                hasProperty("email", is("dejanmatic@gmail.com")),
                hasProperty("email", is("aleksandarmitrovic@gmail.com"))
        ));
    }

    @Test
    public void whenFindAllOnline_thenReturnEmptyList(){
        List<Driver> drivers = driverRepository.findAllOnline();

        Assertions.assertEquals(0, drivers.size());
    }

    @Test
    public void givenOnlineStatus_whenFindByDrivingStatus_thenReturnEmptyList(){
        List<Driver> drivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE);

        Assertions.assertEquals(0, drivers.size());
    }

    @Test
    public void givenOfflineStatus_whenFindByDrivingStatus_thenReturnListOfTwoElements(){
        List<Driver> drivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.OFFLINE);

        Assertions.assertEquals(2, drivers.size());

        assertThat( drivers, contains(
                hasProperty("email", is("dejanmatic@gmail.com")),
                hasProperty("email", is("aleksandarmitrovic@gmail.com"))
        ));
    }

    @Test
    public void givenOnlineBusyStatus_whenFindByDrivingStatus_thenReturnEmptyList(){
        List<Driver> drivers = driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE_BUSY);

        Assertions.assertEquals(0, drivers.size());
    }

    @Test
    public void givenExistentDriverEmail_whenFindByEmail_returnDriver(){
        String email = "aleksandarmitrovic@gmail.com";
        Optional<Driver> driver = driverRepository.findByEmail(email);

        Assertions.assertTrue(driver.isPresent());
    }

    @Test
    public void givenNonExistentDriverEmail_whenFindByEmail_returnEmpty(){
        String email = "aleksandar@gmail.com";
        Optional<Driver> driver = driverRepository.findByEmail(email);

        Assertions.assertTrue(driver.isEmpty());
    }

    @Test
    public void givenEmptyString_whenFindByEmail_returnEmpty(){
        String email = "";
        Optional<Driver> driver = driverRepository.findByEmail(email);

        Assertions.assertTrue(driver.isEmpty());
    }
}
