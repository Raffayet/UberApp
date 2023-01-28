package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.Client;
import com.example.uberbackend.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class ClientRepositoryTests {
    @Autowired
    ClientRepository clientRepository;

    @Test
    void getTokensByEmailTest()
    {
        Client client = new Client();
        client.setEmail("milicamatic@gmail.com");
        client.setTokens(20);
        clientRepository.save(client);

        double actualTokens = clientRepository.getTokensByEmail(client.getEmail());
        Assertions.assertEquals(actualTokens, 20);
    }
}
