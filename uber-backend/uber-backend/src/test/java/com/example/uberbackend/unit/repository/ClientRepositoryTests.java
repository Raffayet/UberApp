package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ClientRepositoryTests {

    @Autowired
    ClientRepository clientRepository;

    // SW-1-2019
    @Test
    public void getTokensByEmailSuccessTest(){
        String email = "sasalukic@gmail.com";
        Double tokens = clientRepository.getTokensByEmail(email);

        assertEquals(10,tokens);
    }

    @Test
    public void getTokensByEmailNoEmailTest(){
        String email = "client@gmail.com";
        Double tokens = clientRepository.getTokensByEmail(email);

        assertNull(tokens);
    }

    @Test
    public void getTokensByEmailNoTokensAssignedTest(){
        String email = "milanpetrovic@gmail.com";
        Double tokens = clientRepository.getTokensByEmail(email);

        assertNull(tokens);
    }

    @Test
    public void findAllRideInvitesHasInvitesTest(){
        String email = "sasalukic@gmail.com";
        List<RideInvite> invites = clientRepository.findAllRideInvites(email);

        assertEquals(invites.size(), 1);
        assertThat( invites, contains(
                hasProperty("emailFrom", is("milicamatic@gmail.com"))
        ));
    }

    @Test
    public void findAllRideInvitesNoInvitesTest(){
        String email = "aleksandarmitrovic@gmail.com";
        List<RideInvite> invites = clientRepository.findAllRideInvites(email);

        assertEquals(invites.size(), 0);
    }

    @Test
    public void findAllRideInvitesNoEmailTest(){
        String email = "client@gmail.com";
        List<RideInvite> invites = clientRepository.findAllRideInvites(email);

        assertEquals(invites.size(), 0);
    }

    @Test
    public void findByEmailSuccessTest(){
        String email = "sasalukic@gmail.com";
        Optional<Client> client = clientRepository.findByEmail(email);

        assertTrue(client.isPresent());
        assertEquals(client.get().getTokens(), 10);
        assertEquals(client.get().getName(), "Sasa");
    }

    @Test
    public void findByEmailFailTest(){
        String email = "client@gmail.com";
        Optional<Client> client = clientRepository.findByEmail(email);

        assertTrue(client.isEmpty());
    }
}
