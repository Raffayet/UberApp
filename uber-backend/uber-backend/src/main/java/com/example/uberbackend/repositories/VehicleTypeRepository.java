package com.example.uberbackend.repositories;

import com.example.uberbackend.model.User;
import com.example.uberbackend.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    @Query("select type from VehicleType")
    Optional<List<String>> getAllVehicleTypes();

    @Query("select vt from VehicleType vt where vt.type = ?1")
    Optional<VehicleType> findByType(String vehicleType);
}
