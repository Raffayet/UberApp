package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {


    Optional<Driver> findByEmail(String email);
}
