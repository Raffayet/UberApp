package com.example.uberbackend.unit;

import com.example.uberbackend.configuration.WebConfig;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.DriveRequestNotFoundException;
import com.example.uberbackend.exception.EmptyStringException;
import com.example.uberbackend.exception.RideInviteNotFoundException;
import com.example.uberbackend.exception.UserNotFoundException;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.RideInviteStatus;
import com.example.uberbackend.repositories.ClientRepository;
import com.example.uberbackend.repositories.DriveRequestRepository;
import com.example.uberbackend.repositories.DriverRepository;
import com.example.uberbackend.repositories.RideInviteRepository;
import com.example.uberbackend.service.ClientService;
import com.example.uberbackend.service.DriverService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
    @Mock
    SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    RideInviteRepository rideInviteRepository;

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

    @Test
    void getTokensByEmailTestSuccess()
    {
        String email = "sasalukic@gmail.com";
        double expectedValue = 10.0;
        Mockito.when(clientRepository.getTokensByEmail(email)).thenReturn(10.0);

        double actualValue = clientService.getTokensByEmail(email);
        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    void getTokensByEmailTestEmptyEmail()
    {
        String email = "";
        Assertions.assertThrows(EmptyStringException.class, () -> clientService.getTokensByEmail(email));
    }

    @Test
    void invitedHasTokensTestSuccessNoPeopleEmails()
    {
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{});
        checkForEnoughTokens.setPricePerPassenger(2);

        String expectedValue = "true";
        String actualValue = clientService.invitedHasTokens(checkForEnoughTokens);
        Assertions.assertEquals(expectedValue, actualValue);
        verify(clientRepository, times(0)).findByEmail(any(String.class));
    }

    @Test
    void invitedHasTokensTestSuccessEnoughTokens()
    {
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{
                "milicamatic@gmail.com"
        });
        checkForEnoughTokens.setPricePerPassenger(2);

        Client milica = new Client();
        milica.setTokens(3);
        Mockito.when(clientRepository.findByEmail(checkForEnoughTokens.getPeopleEmails()[0])).thenReturn(Optional.of(milica));

        String expectedValue = "true";
        String actualValue = clientService.invitedHasTokens(checkForEnoughTokens);
        Assertions.assertEquals(expectedValue, actualValue);
        verify(clientRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void invitedHasTokensTestSuccessNotEnoughTokens()
    {
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{
                "milicamatic@gmail.com"
        });
        checkForEnoughTokens.setPricePerPassenger(2);

        Client milica = new Client();
        milica.setTokens(1);
        Mockito.when(clientRepository.findByEmail(checkForEnoughTokens.getPeopleEmails()[0])).thenReturn(Optional.of(milica));

        String expectedValue = "false";
        String actualValue = clientService.invitedHasTokens(checkForEnoughTokens);
        Assertions.assertEquals(expectedValue, actualValue);
        verify(clientRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void invitedHasTokensTestInvitedPersonNotExist()
    {
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{
                "milicamatic@gmail.com"
        });
        checkForEnoughTokens.setPricePerPassenger(2);
        Mockito.when(clientRepository.findByEmail(checkForEnoughTokens.getPeopleEmails()[0])).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> clientService.invitedHasTokens(checkForEnoughTokens));
        verify(clientRepository, times(1)).findByEmail(any(String.class));
    }

    @Test
    void changeDriveInvitationStatusTestSuccessAccepted()
    {
        InvitationStatusDto invitationStatusDto = new InvitationStatusDto();
        invitationStatusDto.setInvitationId(1L);
        invitationStatusDto.setAccepted(true);

        RideInvite rideInvite = new RideInvite();
        Mockito.when(rideInviteRepository.findById(invitationStatusDto.getInvitationId())).thenReturn(Optional.of(rideInvite));

        RideInviteStatus expectedValue = RideInviteStatus.ACCEPTED;

        clientService.changeDriveInvitationStatus(invitationStatusDto);
        Assertions.assertEquals(expectedValue, rideInvite.getRideInviteStatus());
        verify(rideInviteRepository, times(1)).save(any(RideInvite.class));
    }

    @Test
    void changeDriveInvitationStatusTestSuccessRejected()
    {
        InvitationStatusDto invitationStatusDto = new InvitationStatusDto();
        invitationStatusDto.setInvitationId(1L);
        invitationStatusDto.setAccepted(false);

        RideInvite rideInvite = new RideInvite();
        Mockito.when(rideInviteRepository.findById(invitationStatusDto.getInvitationId())).thenReturn(Optional.of(rideInvite));

        RideInviteStatus expectedValue = RideInviteStatus.REJECTED;

        clientService.changeDriveInvitationStatus(invitationStatusDto);
        Assertions.assertEquals(expectedValue, rideInvite.getRideInviteStatus());
        verify(rideInviteRepository, times(1)).save(any(RideInvite.class));
    }

    @Test
    void changeDriveInvitationStatusTestRideInviteNotFound()
    {
        InvitationStatusDto invitationStatusDto = new InvitationStatusDto();
        invitationStatusDto.setInvitationId(1L);
        invitationStatusDto.setAccepted(true);

        Mockito.when(rideInviteRepository.findById(invitationStatusDto.getInvitationId())).thenReturn(Optional.empty());
        Assertions.assertThrows(RideInviteNotFoundException.class, () -> clientService.changeDriveInvitationStatus(invitationStatusDto));
        verify(rideInviteRepository, times(0)).save(any(RideInvite.class));
    }

    @Test
    void refundTokensTestSuccessNoInvitedPeople()
    {
        //input
        Long requestId = 1L;
        DriveRequest request = new DriveRequest();

        Client initiator = new Client();
        initiator.setTokens(15);

        request.setInitiator(initiator);
        request.setPricePerPassenger(5);

        request.setPeople(new ArrayList<>());

        Mockito.when(driveRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    }

    @Test
    void refundTokensTestSuccessInvitedPeople()
    {
        //input
        Long requestId = 1L;
        DriveRequest request = new DriveRequest();

        Client initiator = new Client();
        initiator.setTokens(15);

        request.setInitiator(initiator);
        request.setPricePerPassenger(5);

        Client client1 = new Client();
        client1.setTokens(10);
        Client client2 = new Client();
        client1.setTokens(8);

        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);
        request.setPeople(clients);

        Mockito.when(driveRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
    }

    @Test
    void refundTokensTestDriveRequestNotFound()
    {
        //input
        Long requestId = 1L;

        Mockito.when(driveRequestRepository.findById(requestId)).thenReturn(Optional.empty());
        Assertions.assertThrows(DriveRequestNotFoundException.class, () -> clientService.refundTokens(requestId));
    }
}
