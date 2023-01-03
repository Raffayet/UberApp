package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Point;
import com.example.uberbackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

}
