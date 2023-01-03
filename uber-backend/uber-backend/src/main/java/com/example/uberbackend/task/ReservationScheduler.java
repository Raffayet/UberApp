package com.example.uberbackend.task;

import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.service.ClientService;
import lombok.SneakyThrows;

public class ReservationScheduler implements Runnable{

    private ClientService clientService;
    private DriveRequestDto dto;

    public ReservationScheduler(ClientService clientService, DriveRequestDto dto){
        this.clientService = clientService;
        this.dto = dto;
    }

    @SneakyThrows
    @Override
    public void run() {
        this.clientService.createDriveRequest(this.dto);
    }
}
