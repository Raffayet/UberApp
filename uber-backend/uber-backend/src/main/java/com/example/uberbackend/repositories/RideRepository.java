package com.example.uberbackend.repositories;

import com.example.uberbackend.dto.HistoryRideDto;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.enums.RideStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends PagingAndSortingRepository<Ride, Long> {

    @Query("select r from Ride r where r.rideStatus = 'WAITING' ")
    List<Ride> findAllActive();

    Page<Ride> findAllByInitiatorEmailAndRideStatus(String email, RideStatus rideStatus, Pageable pageable);

    Page<Ride> findAllByDriverEmailAndRideStatus(String email, RideStatus rideStatus, Pageable pageable);
}
