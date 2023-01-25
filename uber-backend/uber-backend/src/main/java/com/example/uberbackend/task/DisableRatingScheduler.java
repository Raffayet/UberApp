package com.example.uberbackend.task;

import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.dto.RatingExpirationDto;
import com.example.uberbackend.dto.RideReminderDto;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.repositories.RideRepository;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class DisableRatingScheduler implements Runnable{

    private Ride ride;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RideRepository rideRepository;

    public DisableRatingScheduler(Ride ride, SimpMessagingTemplate simpMessagingTemplate, RideRepository rideRepository){
        this.ride = ride;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.rideRepository = rideRepository;
    }

    @SneakyThrows
    @Override
    public void run() {
        ride.setRatingExpired(true);
        this.rideRepository.save(ride);
        simpMessagingTemplate.convertAndSendToUser(ride.getInitiator().getEmail(), "/disable-rating", new RatingExpirationDto(ride.getId(), true));
        for(Client client: ride.getClients())
        {
            simpMessagingTemplate.convertAndSendToUser(client.getEmail(), "/disable-rating", new RatingExpirationDto(ride.getId(), true));
        }
    }
}
