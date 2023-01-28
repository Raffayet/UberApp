package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.repositories.RideRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class RideRepositoryTests {

    @Autowired
    RideRepository rideRepository;

    @Test
    void findAllActiveTest()
    {
        Iterable<Ride> allRides = rideRepository.findAll();
        List<Ride> onlyActive = new ArrayList<>();

        for(Ride ride: allRides)
        {
            if(ride.getRideStatus().equals(RideStatus.WAITING))
            {
                onlyActive.add(ride);
            }
        }

        List<Ride> actualRides = rideRepository.findAllActive();

        assertEquals(onlyActive.size(), actualRides.size());
    }

    @Test
    void findAllEndedTest()
    {
        Iterable<Ride> allRides = rideRepository.findAll();
        List<Ride> onlyEnded = new ArrayList<>();

        for(Ride ride: allRides)
        {
            if(ride.getRideStatus().equals(RideStatus.ENDED))
            {
                onlyEnded.add(ride);
            }
        }

        List<Ride> actualRides = rideRepository.findAllEnded();

        assertEquals(onlyEnded.size(), actualRides.size());
    }

    @Test
    void findAllByInitiatorEmailAndRideStatusTestSuccess()
    {
        String email = "sasalukic@gmail.com";
        RideStatus rideStatus = RideStatus.ENDED;
        Pageable pageable = PageRequest.of(5, 20, Sort.by("id").ascending());

        Iterable<Ride> allRides = rideRepository.findAll();
        List<Ride> endedRidesWithSpecificInitiator = new ArrayList<>();
        for(Ride ride: allRides)
        {
            if(ride.getInitiator().getEmail().equals(email) && ride.getRideStatus().equals(rideStatus))
            {
                endedRidesWithSpecificInitiator.add(ride);
            }
        }

        Page<Ride> actualRides = rideRepository.findAllByInitiatorEmailAndRideStatus(email, rideStatus, pageable);
        assertEquals(endedRidesWithSpecificInitiator.size(), actualRides.getTotalElements());
    }

    @Test
    void findAllByInitiatorEmailAndRideStatusTestInitiatorNotExist()
    {
        String email = "";
        RideStatus rideStatus = RideStatus.ENDED;
        Pageable pageable = PageRequest.of(5, 20, Sort.by("id").ascending());

        Page<Ride> actualRides = rideRepository.findAllByInitiatorEmailAndRideStatus(email, rideStatus, pageable);
        assertEquals(0, actualRides.getTotalElements());
    }

    @Test
    void findAllByDriverEmailAndRideStatusTestSuccess()
    {
        String email = "dejanmatic@gmail.com";
        RideStatus rideStatus = RideStatus.ENDED;
        Pageable pageable = PageRequest.of(5, 20, Sort.by("id").ascending());

        Iterable<Ride> allRides = rideRepository.findAll();
        List<Ride> endedRidesWithSpecificDriver= new ArrayList<>();
        for(Ride ride: allRides)
        {
            if(ride.getDriver().getEmail().equals(email) && ride.getRideStatus().equals(rideStatus))
            {
                endedRidesWithSpecificDriver.add(ride);
            }
        }

        Page<Ride> actualRides = rideRepository.findAllByDriverEmailAndRideStatus(email, rideStatus, pageable);
        assertEquals(endedRidesWithSpecificDriver.size(), actualRides.getTotalElements());
    }

    @Test
    void findAllByDriverEmailAndRideStatusTestDriverNotExist()
    {
        String email = "";
        RideStatus rideStatus = RideStatus.ENDED;
        Pageable pageable = PageRequest.of(5, 20, Sort.by("id").ascending());

        Page<Ride> actualRides = rideRepository.findAllByDriverEmailAndRideStatus(email, rideStatus, pageable);
        assertEquals(0, actualRides.getTotalElements());
    }

}
