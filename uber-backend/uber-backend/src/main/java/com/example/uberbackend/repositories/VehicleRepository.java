package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository  extends JpaRepository<Vehicle, Long> {
}
