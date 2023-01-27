package com.example.uberbackend.unit;

import com.example.uberbackend.configuration.WebConfig;
import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.dto.DriverFoundDto;
import com.example.uberbackend.dto.MapSearchResultDto;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.repositories.ClientRepository;
import com.example.uberbackend.repositories.DriveRequestRepository;
import com.example.uberbackend.repositories.DriverRepository;
import com.example.uberbackend.service.ClientService;
import com.example.uberbackend.service.DriverService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTests {

    @InjectMocks
    ClientService clientService;

    @Mock
    DriverService driverService;
    @Mock
    ClientRepository clientRepository;
    @Mock
    DriveRequestRepository driveRequestRepository;

    @Test
    void createDriveRequestTest() throws IOException {
        DriveRequestDto driveRequestDto = new DriveRequestDto();

        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto(1L,"Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto(2L,"Futoska", "45.11", "19.00")
        );

        driveRequestDto.setInitiatorEmail("sasalukic@gmail.com");
        driveRequestDto.setPeople(new ArrayList<>());
        driveRequestDto.setPrice(250);
        driveRequestDto.setPricePerPassenger(250);
        driveRequestDto.setVehicleType("Standard");
        driveRequestDto.setRouteType("Custom");
        driveRequestDto.setIsReserved(false);
        driveRequestDto.setTimeOfReservation(LocalDateTime.now());
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.now());
        driveRequestDto.setLocations(locations);

        DriverFoundDto driverFoundDto = new DriverFoundDto();
        driverFoundDto.setFound(false);
        driverFoundDto.setDriverEmail("dejanmatic@gmail.com");

        Client client = new Client();

        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.of(client));
//        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.of(client));
        Mockito.when(driverService.findDriverForRequest(any(DriveRequest.class))).thenReturn(driverFoundDto);

        clientService.createDriveRequest(driveRequestDto);

        verify(driveRequestRepository, times(1)).save(any(DriveRequest.class));

//        assertThrows(StudentNotFoundException.class, ()->examService.examApplication(student.getIdentificationNumber(), exam.getId()));

    }
}
