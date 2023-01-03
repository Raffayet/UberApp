package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Ride;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends PagingAndSortingRepository<Ride, Long> {

    @Query("select r from Ride r where r.driver is not null")
    List<Ride> findAllActive();
}
