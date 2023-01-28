package com.example.uberbackend.service;

import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.*;
import com.example.uberbackend.model.*;
import com.example.uberbackend.model.enums.RideInviteStatus;
import com.example.uberbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
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

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RideRepository rideRepository;

    private final RatingRepository ratingRepository;

    public double getTokensByEmail(String email){
        if(email.equals(""))
            throw new EmptyStringException("Empty string!");
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

    public void changeDriveInvitationStatus(InvitationStatusDto invitationStatusDto) {
        Optional<RideInvite> editedRideInvite = this.rideInviteRepository.findById(invitationStatusDto.getInvitationId());
        if(editedRideInvite.isEmpty())
            throw new RideInviteNotFoundException();
        boolean isAccepted = invitationStatusDto.isAccepted();
        editedRideInvite.ifPresent(invite -> invite.setRideInviteStatus(isAccepted ? RideInviteStatus.ACCEPTED : RideInviteStatus.REJECTED));
        editedRideInvite.ifPresent(this.rideInviteRepository::save);
    }

    public void createDriveRequest(DriveRequestDto dto) throws IOException {
        DriveRequest request = new DriveRequest();
        Optional<Client> initiator = clientRepository.findByEmail(dto.getInitiatorEmail());
        if(initiator.isEmpty())
            throw new UsernameNotFoundException("Initiator not found");

        request.setInitiator(initiator.get());

        List<Client> clients = new ArrayList<>();
        for(String email : dto.getPeople()){
            Optional<Client> invited = clientRepository.findByEmail(email);
            if(invited.isEmpty())
                throw new UsernameNotFoundException("Initiator not found");
            clients.add(invited.get());
        }
        if (dto.getPrice() < dto.getPricePerPassenger())
            throw new PriceNotValidException("Price is lower than Price per passenger");

        if(dto.getLocations().size() < 2)
            throw new NotEnoughLocationsException("Not enought locations");

        if(dto.getTimeOfReservation().isBefore(dto.getTimeOfRequestForReservation()))
            throw new NotValidDateTime("Not valid date time");

        request.setPeople(clients);

        request.setPrice(dto.getPrice());
        request.setPricePerPassenger(dto.getPricePerPassenger());
        request.setVehicleType(dto.getVehicleType());
        request.setRouteType(dto.getRouteType());
        request.setIsReserved(dto.getIsReserved());
        request.setTimeOfReservation(dto.getTimeOfReservation());
        request.setTimeOfRequestForReservation(dto.getTimeOfRequestForReservation());
        request.setLocations(dto.getLocations());
        request.setDriversThatRejected(new ArrayList<Driver>());
        driveRequestRepository.save(request);
        DriverFoundDto driverFoundDto = this.driverService.findDriverForRequest(request);

        if (driverFoundDto.isFound()){
            if (!this.drivingCharge(request)){
                throw new PaymentFailedException("Payment failed!");
            }
            this.sendRequestToDriver(request, driverFoundDto);
        }
    }

    private void sendRequestToDriver(DriveRequest request, DriverFoundDto driverFoundDto) {
        RideToTakeDto rideToTakeDto = new RideToTakeDto(request.getId(), request.getLocations().get(0).getDisplayName(), request.getLocations().get(1).getDisplayName(), request.getInitiator().getEmail(), request.getIsReserved(), request.getTimeOfReservation());
        simpMessagingTemplate.convertAndSendToUser(driverFoundDto.getDriverEmail(), "/driver-notification", rideToTakeDto);
    }


    public String invitedHasTokens(CheckForEnoughTokens checkForEnoughTokens) {
        boolean allHaveTokens = true;
        for (String email: checkForEnoughTokens.getPeopleEmails())
        {
            Optional<Client> client = this.clientRepository.findByEmail(email);
            if(client.isEmpty())
                throw new UserNotFoundException();
            if (client.get().getTokens() < checkForEnoughTokens.getPricePerPassenger())
            {
                allHaveTokens = false;
                this.invitedNotHaveTokens(checkForEnoughTokens, client);
                break;
            }
        }

        return String.valueOf(allHaveTokens);
    }

    private void invitedNotHaveTokens(CheckForEnoughTokens checkForEnoughTokens, Optional<Client> invitedClient) {
        invitedClient.ifPresent(client -> simpMessagingTemplate.convertAndSendToUser(checkForEnoughTokens.getInitiatorEmail(), "/invited-person-not-have-tokens", new ResponseToIniciatorDto("error", "Invited person " + client.getEmail() + " doesn't have enough tokens for ride")));
        throw new NotEnoughTokensException("You don't have enough tokens!");
    }

    private boolean drivingCharge(DriveRequest request) {
        Client initiator = request.getInitiator();

        if(request.getPricePerPassenger() == 0)     //situacija kada inicijator casti sve za placanje voznje
        {
            if (initiator.getTokens() < request.getPrice())
                return false;

            initiator.setTokens(initiator.getTokens() - request.getPrice());
            this.clientRepository.save(initiator);

            return true;
        }

        if(initiator.getTokens() < request.getPricePerPassenger())          //situacija kada je split fare izabran
            return false;

        initiator.setTokens(initiator.getTokens() - request.getPricePerPassenger());
        this.clientRepository.save(initiator);

        for (Client client : request.getPeople())
        {
            if(client.getTokens() < request.getPricePerPassenger())
                return false;

            client.setTokens(client.getTokens() - request.getPricePerPassenger());
            this.clientRepository.save(client);
        }

        return true;
    }

    public void refundTokens(Long requestId) {
        Optional<DriveRequest> request = this.driveRequestRepository.findById(requestId);
        if(request.isEmpty())
            throw new DriveRequestNotFoundException();
        refundTokensToClient(request.get().getInitiator(), request.get().getPricePerPassenger());
        if(request.get().getPrice() != request.get().getPricePerPassenger())
        {
            for(Client client: request.get().getPeople())
            {
                refundTokensToClient(client, request.get().getPricePerPassenger());
            }
        }
    }

    public void refundTokensAfterAccepting(Long requestId) {
        Ride ride = this.rideRepository.findById(requestId).orElseThrow(RideNotFoundException::new);

        refundTokensToClient(ride.getInitiator(), ride.getPricePerPassenger());
        if(ride.getPrice() != ride.getPricePerPassenger())
        {
            for(Client client: ride.getClients())
            {
                refundTokensToClient(client, ride.getPricePerPassenger());
            }
        }
    }

    private void refundTokensToClient(Client client, double priceToRefund) {
        double currentBalance = client.getTokens();
        client.setTokens(currentBalance + priceToRefund);
        this.clientRepository.save(client);
    }
}
