package com.example.uberbackend.service;

import com.example.uberbackend.dto.DriveInvitationDto;
import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.model.enums.RideInviteStatus;
import com.example.uberbackend.repositories.ClientRepository;
import com.example.uberbackend.repositories.DriveRequestRepository;
import com.example.uberbackend.repositories.RideInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    private final RideInviteRepository rideInviteRepository;

    private final DriveRequestRepository driveRequestRepository;

    private final DriverService driverService;

    public double getTokensByEmail(String email){
        return clientRepository.getTokensByEmail(email);
    }

    public List<RideInvite> findAllRideInvites(String userEmail) {
        return this.rideInviteRepository.findAllRideInvites(userEmail);
    }

    public void createDriveInvitation(DriveInvitationDto driveInvitationDTO) {
        for(String personEmail : driveInvitationDTO.getEmailsTo())
        {
            RideInvite newRideInvite = new RideInvite();
            newRideInvite.setRideInviteStatus(RideInviteStatus.PENDING);
            newRideInvite.setEmailFrom(driveInvitationDTO.getEmailFrom());
            newRideInvite.setFirstLocation(driveInvitationDTO.getFirstLocation());
            newRideInvite.setDestination(driveInvitationDTO.getDestination());
            newRideInvite.setPriceToPay(driveInvitationDTO.getPriceToPay());
            newRideInvite.setEmailTo(personEmail);
            this.rideInviteRepository.save(newRideInvite);
        }
    }

    public void changeDriveInvitationStatus(HashMap<String, String> dto) {
        Optional<RideInvite> editedRideInvite = this.rideInviteRepository.findById(Long.parseLong(dto.get("id")));
        boolean isAccepted = Boolean.parseBoolean(dto.get("isAccepted"));
        editedRideInvite.ifPresent(invite -> invite.setRideInviteStatus(isAccepted ? RideInviteStatus.ACCEPTED : RideInviteStatus.REJECTED));
        editedRideInvite.ifPresent(this.rideInviteRepository::save);
    }

    public void createDriveRequest(DriveRequestDto dto) {
        DriveRequest request = new DriveRequest();
        Optional<Client> initiator = clientRepository.findByEmail(dto.getInitiatorEmail());
        initiator.ifPresent(request::setInitiator);

        List<Client> clients = new ArrayList<>();
        for(String email : dto.getPeople()){
            Optional<Client> invited = clientRepository.findByEmail(email);
            invited.ifPresent(clients::add);
        }
        request.setPeople(clients);

        request.setPrice(dto.getPrice());
        request.setPricePerPassenger(dto.getPricePerPassenger());
        request.setVehicleType(dto.getVehicleType());
        request.setRouteType(dto.getRouteType());

        request.setLocations(dto.getLocations());
        request.setDriversThatRejected(new ArrayList<Driver>());

        driveRequestRepository.save(request);
        this.driverService.findDriverForRequest(request);
    }
}
