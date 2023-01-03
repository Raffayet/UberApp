package com.example.uberbackend.service;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.NotFoundException;
import com.example.uberbackend.model.Point;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.repositories.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class RideService {

    private final RideRepository rideRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MapService mapService;

    public Page<Ride> findAll(Pageable pageable) {
        return rideRepository.findAll(pageable);
    }


    public List<MapRideDto> getActiveRides(){
        List<Ride> rides = rideRepository.findAllActive();

        List<MapRideDto> mapRideDtos = new ArrayList<>();

        for (Ride ride :rides) {
            PathInfoDto pathInfoDto = null;
            List<Point> points = ride.getLocations()
                    .stream()
                    .map(c -> new Point(c.getLat(), c.getLon()))
                    .collect(Collectors.toList());
            if(ride.getRouteType().equals("Custom") ) {
                try {
                    pathInfoDto = mapService.getCustomRoute(points);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(ride.getRouteType().equals("Alternative")){
                try {
                    pathInfoDto = mapService.getAlternativeRoute(points);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    pathInfoDto = mapService.getOptimalRoute(points);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            MapRideDto mapRideDto = new MapRideDto(ride);
            List<LocationDto> locationDtos = pathInfoDto.getAtomicPoints()
                    .stream().map(c -> new LocationDto(c.getLat(), c.getLng())).collect(Collectors.toList());
            mapRideDto.setAtomicPoints(locationDtos);

            mapRideDtos.add(mapRideDto);
        }

        return mapRideDtos;

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
