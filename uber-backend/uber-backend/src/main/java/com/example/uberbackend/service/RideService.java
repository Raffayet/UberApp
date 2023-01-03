package com.example.uberbackend.service;
import com.example.uberbackend.dto.LocationDto;
import com.example.uberbackend.dto.MapDriverDto;
import com.example.uberbackend.dto.MapRideDto;
import com.example.uberbackend.exception.NotFoundException;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.repositories.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

@Service
@AllArgsConstructor
@Transactional
public class RideService {

    private final RideRepository rideRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public Page<Ride> findAll(Pageable pageable) {
        return rideRepository.findAll(pageable);
    }


    public MapRideDto getRide(){
        MapDriverDto mapDriverDto = new MapDriverDto();
        mapDriverDto.setId(1L);
        mapDriverDto.setLatitude(45.235866);
        mapDriverDto.setLongitude(19.807387);

        MapRideDto mapRideDto = new MapRideDto();
        mapRideDto.setDriver(mapDriverDto);
        mapRideDto.setRideId(1L);
        mapRideDto.setStatus(RideStatus.WAITING);
        List<LocationDto> locations = new ArrayList<>();
        locations.add(new LocationDto(45.265435, 19.847805));
        locations.add(new LocationDto(45.255521, 19.845071));
        locations.add(new LocationDto(45.223481, 19.847990));
        mapRideDto.setRoutePoints(locations);

        return mapRideDto;

    }

    public Ride changeRide(long id) {
        Ride ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        ride.setRideStatus(RideStatus.ENDED);
        return this.rideRepository.save(ride);

    }

    public Double calculatePrice(String vehicleType, double totalDistance) {
         HashMap<String, Double> vehicleTypeMap = new HashMap<String, Double>();
         vehicleTypeMap.put("Regular", 200.0);   //pocetna cena u zavisnosti od tipa vozila
         vehicleTypeMap.put("Baby Seats", 300.0);
         vehicleTypeMap.put("Pet Seats", 250.0);

         double price = (vehicleTypeMap.get(vehicleType) + (totalDistance / 1000) * 120) / 109.94;
         return Math.round(price * 100.0) / 100.0;
    }

    public Page<Ride> findAllByUserEmail(Pageable paging, String email) {
        return rideRepository.findAllByInitiatorEmail(email, paging);
    }
}
