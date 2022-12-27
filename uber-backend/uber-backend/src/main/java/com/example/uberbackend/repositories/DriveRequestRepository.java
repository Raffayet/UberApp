package com.example.uberbackend.repositories;

import com.example.uberbackend.model.DriveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveRequestRepository extends JpaRepository<DriveRequest, Long> {
}
