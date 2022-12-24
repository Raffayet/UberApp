package com.example.uberbackend.service;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.repositories.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
@AllArgsConstructor
public class RideService {

    private final RideRepository rideRepository;

    public Page<Ride> findAll(Pageable pageable) {
        return rideRepository.findAll(pageable);
    }
}
