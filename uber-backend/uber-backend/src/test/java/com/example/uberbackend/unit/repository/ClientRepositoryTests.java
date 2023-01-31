package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.FavoriteRoute;
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
    public void whenGetTokensByEmail_thenReturnNoOfTokens(){
        String email = "sasalukic@gmail.com";
        Double tokens = clientRepository.getTokensByEmail(email);

        assertEquals(10,tokens);
    }

    @Test
    public void givenNonexistentEmail_whenGetTokensByEmail_thenReturnNull(){
        String email = "client@gmail.com";
        Double tokens = clientRepository.getTokensByEmail(email);

        assertNull(tokens);
    }

    @Test
    public void givenEmailWithNoAssignedTokens_whenGetTokensByEmail_thenReturnNull(){
        String email = "milanpetrovic@gmail.com";
        Double tokens = clientRepository.getTokensByEmail(email);

        assertNull(tokens);
    }

    @Test
    public void givenExistentEmail_whenFindAllRideInvites_thenReturnNonEmptyList(){
        String email = "sasalukic@gmail.com";
        List<RideInvite> invites = clientRepository.findAllRideInvites(email);

        assertEquals(invites.size(), 1);
        assertThat( invites, contains(
                hasProperty("emailFrom", is("milicamatic@gmail.com"))
        ));
    }

    @Test
    public void givenExistentEmail_whenFindAllRideInvites_thenReturnEmptyList(){
        String email = "aleksandarmitrovic@gmail.com";
        List<RideInvite> invites = clientRepository.findAllRideInvites(email);

        assertEquals(invites.size(), 0);
    }

    @Test
    public void givenNonexistentEmail_whenFindAllRideInvites_thenReturnEmptyList(){
        String email = "client@gmail.com";
        List<RideInvite> invites = clientRepository.findAllRideInvites(email);

        assertEquals(invites.size(), 0);
    }

    @Test
    public void givenExistentClientEmail_whenFindByEmail_thenReturnSuccess(){
        String email = "sasalukic@gmail.com";
        Optional<Client> client = clientRepository.findByEmail(email);

        assertTrue(client.isPresent());
        assertEquals(client.get().getTokens(), 10);
        assertEquals(client.get().getName(), "Sasa");
    }

    @Test
    public void givenNonexistentClientEmail_whenFindByEmail_thenDontFind(){
        String email = "client@gmail.com";
        Optional<Client> client = clientRepository.findByEmail(email);

        assertTrue(client.isEmpty());
    }

    // GetFavoriteRoutesByEmail - SW-1-2019
    @Test
    public void givenEmptyString_whenGetFavoriteRoutesByEmail_returnEmptyList(){
        String email = "";
        List<FavoriteRoute> routes = clientRepository.getFavoriteRoutesByEmail(email);

        assertEquals(0, routes.size());
    }

    @Test
    public void givenNonexistentClientEmail_whenGetFavoriteRoutesByEmail_returnEmptyList(){
        String email = "client@gmail.com";
        List<FavoriteRoute> routes = clientRepository.getFavoriteRoutesByEmail(email);

        assertEquals(0, routes.size());
    }

    @Test
    public void givenExistentClientEmail_whenGetFavoriteRoutesByEmail_returnListOfSizeOne(){
        String email = "sasalukic@gmail.com";
        List<FavoriteRoute> routes = clientRepository.getFavoriteRoutesByEmail(email);

        assertEquals(1, routes.size());
    }
}
