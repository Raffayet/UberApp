package com.example.uberbackend.repositories;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.enums.DrivingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("SELECT d FROM Driver d")
    List<Driver> findAvailableDrivers();

    List<Driver> findByDrivingStatusEquals(DrivingStatus drivingStatus);

    Optional<Driver> findByEmail(String email);
}
