package com.example.uberbackend.service;

import com.example.uberbackend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public double getTokensByEmail(String email){
        return clientRepository.getTokensByEmail(email);
    }
}
