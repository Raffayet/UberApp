package com.example.uberbackend.service;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.repositories.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class RideService {

    private final RideRepository rideRepository;

    public Page<Ride> findAll(Pageable pageable) {
        return rideRepository.findAll(pageable);
    }

    public Double calculatePrice(String vehicleType, double totalDistance) {
         HashMap<String, Double> vehicleTypeMap = new HashMap<String, Double>();
         vehicleTypeMap.put("Regular", 200.0);   //pocetna cena u zavisnosti od tipa vozila
         vehicleTypeMap.put("Baby Seats", 300.0);
         vehicleTypeMap.put("Pet Seats", 250.0);

         double price = (vehicleTypeMap.get(vehicleType) + totalDistance * 120) / 109.94;
         return (double) Math.round(price * 100.0) / 100.0 / 1000;
    }
}
