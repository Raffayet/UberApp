package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends PagingAndSortingRepository<Ride, Long> {

    @Query(value="SELECT r FROM Ride r  WHERE r.initiator.email = ?1", nativeQuery=true)
    Page<Ride> findAllByInitiatorEmail(String email, Pageable pageable);
}
