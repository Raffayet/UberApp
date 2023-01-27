package com.example.uberbackend.unit;

import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.DriveRequestNotFoundException;
import com.example.uberbackend.exception.DriverNotFoundException;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.repositories.DriveRequestRepository;
import com.example.uberbackend.repositories.DriverRepository;
import com.example.uberbackend.repositories.RideRepository;
import com.example.uberbackend.service.DriverService;
import com.example.uberbackend.service.MapService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class DriverServiceTests {

    @InjectMocks
    DriverService driverService;
    @Mock
    DriverRepository driverRepository;
    @Mock
    DriveRequestRepository driveRequestRepository;
    @Mock
    RideRepository rideRepository;
    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @Test
    void findDriverForRequestTestSuccessFoundAvailable() throws IOException {
        //input
        DriveRequest request = new DriveRequest();

        Client client = new Client();
        request.setInitiator(client);

        request.setPeople(new ArrayList<Client>());
        request.setPrice(5);
        request.setPricePerPassenger(5);
        request.setVehicleType("Standard");
        request.setIsReserved(false);
        request.setTimeOfReservation(LocalDateTime.now());
        request.setTimeOfRequestForReservation(LocalDateTime.now());

        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto(1L,"Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto(2L,"Futoska", "45.11", "19.00")
        );
        request.setLocations(locations);

        request.setDriversThatRejected(new ArrayList<Driver>());

        List<Driver> availableDrivers = new ArrayList<Driver>();
        Driver driver = new Driver();
        driver.setEmail("dejanmatic@gmail.com");
        driver.setDrivingStatus(DrivingStatus.ONLINE);
        availableDrivers.add(driver);
        List<Point> points = new ArrayList<>();

        PathInfoDto pathInfoDto = new PathInfoDto();

        DriverFoundDto expectedResult = new DriverFoundDto();
        expectedResult.setFound(true);
        expectedResult.setDriverEmail(any(String.class));

        Mockito.when(driverRepository.findByDrivingStatusEquals(DrivingStatus.ONLINE)).thenReturn(availableDrivers);

        DriverFoundDto actualResult = driverService.findDriverForRequest(request);
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void assignDriveToDriverTestSuccess()
    {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic@gmail.com");
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");
        driveAssignatureDto.setRequestId(1L);

        Driver driver = new Driver();
        driver.setRides(new ArrayList<Ride>());

        DriveRequest driveRequest = new DriveRequest();
        driveRequest.setPeople(new ArrayList<Client>());
        driveRequest.setInitiator(new Client());
        driveRequest.setPrice(3);
        driveRequest.setPricePerPassenger(3);
        driveRequest.setVehicleType("Standard");
        driveRequest.setRouteType("Custom");
        driveRequest.setIsReserved(true);
        driveRequest.setTimeOfReservation(LocalDateTime.now());
        driveRequest.setTimeOfRequestForReservation(LocalDateTime.now());

        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto(1L,"Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto(2L,"Futoska", "45.11", "19.00")
        );
        driveRequest.setLocations(locations);

        Mockito.when(driverRepository.findByEmail(driveAssignatureDto.getDriverEmail())).thenReturn(Optional.of(driver));
        Mockito.when(driveRequestRepository.findById(driveAssignatureDto.getRequestId())).thenReturn(Optional.of(driveRequest));

        driverService.assignDriveToDriver(driveAssignatureDto);

        //assert
        verify(rideRepository, times(1)).save(any(Ride.class));
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void assignDriveToDriverTestDriverNotFound()
    {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic@gmail.com");
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");
        driveAssignatureDto.setRequestId(1L);

        DriveRequest driveRequest = new DriveRequest();
        driveRequest.setPeople(new ArrayList<Client>());
        driveRequest.setInitiator(new Client());
        driveRequest.setPrice(3);
        driveRequest.setPricePerPassenger(3);
        driveRequest.setVehicleType("Standard");
        driveRequest.setRouteType("Custom");
        driveRequest.setIsReserved(true);
        driveRequest.setTimeOfReservation(LocalDateTime.now());
        driveRequest.setTimeOfRequestForReservation(LocalDateTime.now());

        Mockito.when(driverRepository.findByEmail(driveAssignatureDto.getDriverEmail())).thenReturn(Optional.empty());

        //assert
        Assertions.assertThrows(DriverNotFoundException.class,
                () -> driverService.assignDriveToDriver(driveAssignatureDto));

        verify(rideRepository, times(0)).save(any(Ride.class));
        verify(driverRepository, times(0)).save(any(Driver.class));
    }

    @Test
    void assignDriveToDriverTestDriveRequestNotFound()
    {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic@gmail.com");
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");
        driveAssignatureDto.setRequestId(1L);

        Driver driver = new Driver();
        driver.setRides(new ArrayList<Ride>());

        Mockito.when(driverRepository.findByEmail(driveAssignatureDto.getDriverEmail())).thenReturn(Optional.of(driver));
        Mockito.when(driveRequestRepository.findById(driveAssignatureDto.getRequestId())).thenReturn(Optional.empty());

        //assert
        Assertions.assertThrows(DriveRequestNotFoundException.class,
                () -> driverService.assignDriveToDriver(driveAssignatureDto));

        verify(rideRepository, times(0)).save(any(Ride.class));
        verify(driverRepository, times(0)).save(any(Driver.class));
    }
}
