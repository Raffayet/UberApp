package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Ride;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends PagingAndSortingRepository<Ride, Long> {
}
