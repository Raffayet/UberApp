package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.model.enums.RideInviteStatus;
import com.example.uberbackend.repositories.RideInviteRepository;
import com.example.uberbackend.repositories.VehicleTypeRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class RideInviteRepositoryTests {
    @Autowired
    RideInviteRepository rideInviteRepository;

    @Test
    void getUserEmailsSuccessTest()
    {
        String email = "sasalukic@gmail.com";
        List<RideInvite> rideInvites = rideInviteRepository.findAllRideInvites(email);

        assertEquals(1, rideInvites.size());

        assertEquals("sasalukic@gmail.com",rideInvites.get(0).getEmailTo());
        assertEquals(RideInviteStatus.PENDING,rideInvites.get(0).getRideInviteStatus());

    }

    @Test
    void getUserEmailsNoSuchUserTest()
    {
        String email = "bad";
        List<RideInvite> rideInvites = rideInviteRepository.findAllRideInvites(email);

        assertEquals(0, rideInvites.size());
    }
}
