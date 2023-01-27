package com.example.uberbackend.unit;

import com.example.uberbackend.configuration.WebConfig;
import com.example.uberbackend.dto.DriveInvitationDto;
import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.dto.DriverFoundDto;
import com.example.uberbackend.dto.MapSearchResultDto;
import com.example.uberbackend.exception.*;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.model.enums.RideInviteStatus;
import com.example.uberbackend.repositories.ClientRepository;
import com.example.uberbackend.repositories.DriveRequestRepository;
import com.example.uberbackend.repositories.RideInviteRepository;
import com.example.uberbackend.repositories.RideRepository;
import com.example.uberbackend.service.ClientService;
import com.example.uberbackend.service.DriverService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
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
    RideInviteRepository rideInviteRepository;
    @Mock
    RideRepository rideRepository;

    @Test
    void createDriveRequestSuccessTest() throws IOException {
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
        Mockito.when(driverService.findDriverForRequest(any(DriveRequest.class))).thenReturn(driverFoundDto);

        clientService.createDriveRequest(driveRequestDto);

        verify(driveRequestRepository, times(1)).save(any(DriveRequest.class));
    }

    @Test
    void createDriveRequestUsernameNotFoundExTest(){
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

        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,()->clientService.createDriveRequest(driveRequestDto));
        verify(clientRepository, times(1)).findByEmail(driveRequestDto.getInitiatorEmail());
    }

    @Test
    void createDriveRequestDriverNotFoundExTest() throws IOException {
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

        Client client = new Client();

        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.of(client));
        Mockito.when(driverService.findDriverForRequest(any(DriveRequest.class))).thenReturn(null);

        assertThrows(NullPointerException.class,()->clientService.createDriveRequest(driveRequestDto));
        verify(clientRepository, times(1)).findByEmail(driveRequestDto.getInitiatorEmail());
        verify(driveRequestRepository, times(1)).save(any(DriveRequest.class));
    }

    @Test
    void createDriveRequestNotEnoughTokensExTest() throws IOException {
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

        Client client = new Client();
        client.setTokens(10);

        DriverFoundDto driverFoundDto = new DriverFoundDto();
        driverFoundDto.setFound(true);
        driverFoundDto.setDriverEmail("dejanmatic@gmail.com");

        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.of(client));
        Mockito.when(driverService.findDriverForRequest(any(DriveRequest.class))).thenReturn(driverFoundDto);

        assertThrows(PaymentFailedException.class,()->clientService.createDriveRequest(driveRequestDto));
        verify(clientRepository, times(1)).findByEmail(driveRequestDto.getInitiatorEmail());
        verify(driveRequestRepository, times(1)).save(any(DriveRequest.class));
    }

    @Test
    void createDriveRequestPriceLowerThanPricePerPassengerExTest() throws IOException {
        DriveRequestDto driveRequestDto = new DriveRequestDto();

        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto(1L,"Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto(2L,"Futoska", "45.11", "19.00")
        );

        driveRequestDto.setInitiatorEmail("sasalukic@gmail.com");
        driveRequestDto.setPeople(new ArrayList<>());
        driveRequestDto.setPrice(250);
        driveRequestDto.setPricePerPassenger(300);
        driveRequestDto.setVehicleType("Standard");
        driveRequestDto.setRouteType("Custom");
        driveRequestDto.setIsReserved(false);
        driveRequestDto.setTimeOfReservation(LocalDateTime.now());
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.now());
        driveRequestDto.setLocations(locations);

        Client client = new Client();

        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.of(client));
        Mockito.when(driverService.findDriverForRequest(any(DriveRequest.class))).thenReturn(null);

        assertThrows(PriceNotValidException.class,()->clientService.createDriveRequest(driveRequestDto));
        verify(clientRepository, times(1)).findByEmail(driveRequestDto.getInitiatorEmail());
    }

    @Test
    void createDriveRequestOneLocationExTest() throws IOException {
        DriveRequestDto driveRequestDto = new DriveRequestDto();

        List<MapSearchResultDto> locations = List.of(
                new MapSearchResultDto(1L, "Rumenacka", "45.11", "19.00")
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

        Client client = new Client();

        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.of(client));
        Mockito.when(driverService.findDriverForRequest(any(DriveRequest.class))).thenReturn(null);

        assertThrows(NotEnoughLocationsException.class,()->clientService.createDriveRequest(driveRequestDto));
        verify(clientRepository, times(1)).findByEmail(driveRequestDto.getInitiatorEmail());
    }
    @Test
    void createDriveRequestTimeOfReservationExTest() throws IOException {
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
        driveRequestDto.setTimeOfReservation(LocalDateTime.of(2022,11,2,5,0));
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.of(2023,1,2,5,0));
        driveRequestDto.setLocations(locations);

        Client client = new Client();

        Mockito.when(clientRepository.findByEmail(driveRequestDto.getInitiatorEmail())).thenReturn(Optional.of(client));
        Mockito.when(driverService.findDriverForRequest(any(DriveRequest.class))).thenReturn(null);

        assertThrows(NotValidDateTime.class,()->clientService.createDriveRequest(driveRequestDto));
        verify(clientRepository, times(1)).findByEmail(driveRequestDto.getInitiatorEmail());
    }

    @Test
    void createDriveInvitationSuccessTest(){
        DriveInvitationDto driveInvitationDto = new DriveInvitationDto();
        driveInvitationDto.setFirstLocation("Rumenacka, Novi Sad");
        driveInvitationDto.setDestination("Futoska, Novi Sad");
        driveInvitationDto.setEmailFrom("sasalukic@gmail.com");
        driveInvitationDto.setRideInviteStatus(RideInviteStatus.PENDING);
        driveInvitationDto.setEmailsTo(List.of("milicamatic@gmail.com", "strahinjapavlovic@gmail.com"));
        driveInvitationDto.setPriceToPay(10);

        clientService.createDriveInvitation(driveInvitationDto);
        verify(rideInviteRepository, times(2)).save(any(RideInvite.class));
    }
    @Test
    void createDriveInvitationNoEmailsTest(){
        DriveInvitationDto driveInvitationDto = new DriveInvitationDto();
        driveInvitationDto.setFirstLocation("Rumenacka, Novi Sad");
        driveInvitationDto.setDestination("Futoska, Novi Sad");
        driveInvitationDto.setEmailFrom("sasalukic@gmail.com");
        driveInvitationDto.setRideInviteStatus(RideInviteStatus.PENDING);
        driveInvitationDto.setEmailsTo(new ArrayList<>());
        driveInvitationDto.setPriceToPay(10);

        clientService.createDriveInvitation(driveInvitationDto);
        verify(rideInviteRepository, times(0)).save(any(RideInvite.class));
    }

    @Test
    void findAllRideInvitesSuccessTest(){
        List <RideInvite> rideInvites = List.of(
                new RideInvite(1L, "milicamatic@gmail.com", "sasalukic@gmail.com", "Rumenacka, Novi Sad", "Futoska, Novi Sad", 10, RideInviteStatus.PENDING),
                new RideInvite(1L, "strahinjapavlovic@gmail.com", "sasalukic@gmail.com", "Bul. oslobodjenja , Novi Sad", "Fruskogorska, Novi Sad", 10, RideInviteStatus.PENDING)
        );
        String email = "sasalukic@gmail.com";
        Mockito.when(rideInviteRepository.findAllRideInvites(email)).thenReturn(rideInvites);

        assertEquals(clientService.findAllRideInvites(email), rideInvites);
        verify(rideInviteRepository, times(1)).findAllRideInvites(email);
    }

    @Test
    void findAllRideInvitesEmptyTest(){
        List <RideInvite> rideInvites = new ArrayList<>();
        String email = "sasalukic@gmail.com";
        Mockito.when(rideInviteRepository.findAllRideInvites(email)).thenReturn(rideInvites);

        assertEquals(clientService.findAllRideInvites(email), rideInvites);
        verify(rideInviteRepository, times(1)).findAllRideInvites(email);
    }

    @Test
    void findAllRideInvitesNullTest(){
        String email = "sasalukic@gmail.com";
        Mockito.when(rideInviteRepository.findAllRideInvites(email)).thenReturn(null);

        assertNull(clientService.findAllRideInvites(email));
        verify(rideInviteRepository, times(1)).findAllRideInvites(email);
    }

    @Test
    void refundTokensAfterAcceptingSuccessTest(){
        Client initiator = new Client();
        initiator.setEmail("sasalukic@gmail.com");
        initiator.setTokens(15);

        Client passenger1 = new Client();
        passenger1.setEmail("milicamatic@gmail.com");
        passenger1.setTokens(8);
        Client passenger2 = new Client();
        passenger2.setEmail("strahinjapavlovic@gmail.com");
        passenger2.setTokens(5);

        Ride ride = new Ride();
        ride.setId(1L);
        ride.setPrice(10);
        ride.setPricePerPassenger(5);
        ride.setInitiator(initiator);
        ride.setClients(List.of(passenger1, passenger2));

        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        clientService.refundTokensAfterAccepting(1L);
        verify(clientRepository, times(3)).save(any(Client.class));
        verify(rideRepository, times(1)).findById(anyLong());
    }
    @Test
    void refundTokensAfterAcceptingRideNotFoundExTest(){
        Mockito.when(rideRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class ,()->clientService.refundTokensAfterAccepting(1L));
        verify(clientRepository, times(0)).save(any(Client.class));
        verify(rideRepository, times(1)).findById(anyLong());
    }



}
