package com.example.uberbackend.repositories;

import com.example.uberbackend.dto.MapSearchResultDto;
import com.example.uberbackend.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapSearchResultRepository extends JpaRepository<MapSearchResultDto, Long> {
}
