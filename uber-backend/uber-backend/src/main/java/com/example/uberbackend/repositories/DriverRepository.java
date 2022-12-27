package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("SELECT d FROM Driver d")
    List<Driver> findAvailableDrivers();
}
