package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Ride;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends PagingAndSortingRepository<Ride, Long> {

    @Query("select r from Ride r where r.rideStatus = 'WAITING' ")
    List<Ride> findAllActive();

    @Query(value="SELECT r FROM Ride r  WHERE r.initiator.email = ?1", nativeQuery=true)
    Page<Ride> findAllByInitiatorEmail(String email, Pageable pageable);

    @Query("select r from Ride r where r.rideStatus = 'ENDED'")
    List<Ride> findAllEnded();
}
