package com.example.uberbackend.task;

import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.dto.RideReminderDto;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.service.ClientService;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class NotificationScheduler implements Runnable{

    private DriveRequestDto driveRequestDto;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private int numberOfMinutes;

    public NotificationScheduler(DriveRequestDto driveRequestDto, SimpMessagingTemplate simpMessagingTemplate, int numberOfMinutes){
        this.driveRequestDto = driveRequestDto;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.numberOfMinutes = numberOfMinutes;
    }

    @SneakyThrows
    @Override
    public void run() {
        simpMessagingTemplate.convertAndSendToUser(driveRequestDto.getInitiatorEmail(), "/remind-for-ride", new RideReminderDto(driveRequestDto.getLocations().get(0).getDisplayName(), driveRequestDto.getLocations().get(1).getDisplayName(), this.numberOfMinutes));
        for(String personEmail: driveRequestDto.getPeople())
        {
            simpMessagingTemplate.convertAndSendToUser(personEmail, "/remind-for-ride", new RideReminderDto(driveRequestDto.getLocations().get(0).getDisplayName(), driveRequestDto.getLocations().get(1).getDisplayName(), this.numberOfMinutes));
        }
    }
}
