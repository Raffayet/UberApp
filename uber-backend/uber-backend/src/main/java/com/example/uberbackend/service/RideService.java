package com.example.uberbackend.service;
import com.example.uberbackend.dto.*;
import com.example.uberbackend.exception.NotFoundException;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.Point;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.VehicleType;
import com.example.uberbackend.model.enums.DrivingStatus;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.repositories.DriverRepository;
import com.example.uberbackend.repositories.RideRepository;
import com.example.uberbackend.repositories.VehicleTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class RideService {

    private final RideRepository rideRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MapService mapService;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final DriverRepository driverRepository;

    public Page<Ride> findAll(Pageable pageable) {
        return rideRepository.findAll(pageable);
    }


    public List<MapRideDto> getActiveRides(){
        List<Ride> rides = rideRepository.findAllActive();

        List<MapRideDto> mapRideDtos = new ArrayList<>();

        for (Ride ride :rides) {
            if(ride.getTimeOfReservation()!=null && ride.getTimeOfReservation().isAfter(LocalDateTime.now()))
                continue;
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


    public Ride endRide(long id) {
        Ride ride = this.rideRepository.findById(id).orElseThrow(() -> new NotFoundException("Ride does not exist!"));
        ride.setRideStatus(RideStatus.ENDED);
        this.rideRepository.save(ride);

        Driver driver = this.driverRepository.findById(ride.getDriver().getId()).orElse(null);
        boolean newRideExist = false;
        for (Ride driverRide:driver.getRides())
            if(driverRide.getRideStatus() == RideStatus.WAITING)
                newRideExist = true;

        if(!newRideExist){
            driver.setDrivingStatus(DrivingStatus.ONLINE);
            this.driverRepository.save(driver);
        }

        return ride;
    }

    public Double calculatePrice(String vehicleType, double totalDistance) {
        VehicleType vehicleType1 = vehicleTypeRepository.findByType(vehicleType).orElse(null);
        double coefficient = vehicleType1.getCoefficient();

        double price = (coefficient*150 + (totalDistance / 1000) * 120) / 109.94;
        return Math.round(price * 100.0) / 100.0;
    }

    public Page<Ride> findEndedRidesByEmail(Pageable paging, String email) {
        Page<Ride> endedRides = rideRepository.findAllByInitiatorEmailAndRideStatus(email, RideStatus.ENDED, paging);
        return endedRides;
//        List<HistoryRideDto> historyRideDtos = new ArrayList<HistoryRideDto>();
//        for(Ride endedRide: endedRides)
//        {
//            HistoryRideDto historyRideDto = new HistoryRideDto();
//            historyRideDto.setId(endedRide.getId());
//            historyRideDto.setFirstLocation(endedRide.getLocations().get(0).getDisplayName());
//            historyRideDto.setDestination(endedRide.getLocations().get(1).getDisplayName());
//            historyRideDto.setPrice(endedRide.getPrice());
//            historyRideDto.setStartDate(String.valueOf(endedRide.getTimeOfReservation()));      //ovo promeniti na pravi start i end time
//            historyRideDto.setEndDate(String.valueOf(endedRide.getTimeOfReservation()));
//            historyRideDtos.add(historyRideDto);
//        }
    }

    public void updateRideStatus(MapRideDto mapRideDto) {
        LocationDto startPoint = mapRideDto.getAtomicPoints().get(0);
        if(mapRideDto.getDriver().getLatitude() == startPoint.getLatitude() && mapRideDto.getDriver().getLongitude() == startPoint.getLongitude()){
            mapRideDto.setStatus(RideStatus.STARTED);
            Ride ride = rideRepository.findById(mapRideDto.getId()).orElse(null);
            ride.setRideStatus(RideStatus.STARTED);
            rideRepository.save(ride);
        }
    }

    public void checkIfRideIsCanceled(MapRideDto mapRideDto) {
        Ride ride = rideRepository.findById(mapRideDto.getId()).orElse(null);
        if(ride.getRideStatus() == RideStatus.CANCELED)
            mapRideDto.setStatus(RideStatus.CANCELED);

    }

    public void aproxDuration(MapRideDto mapRideDto) {
        int distance = 0;
        if(mapRideDto.getStatus() == RideStatus.WAITING)
            distance = getRemainingPointsSize(mapRideDto.getAtomicPointsBeforeRide(), mapRideDto.getDriver());
        else
            distance = getRemainingPointsSize(mapRideDto.getAtomicPoints(), mapRideDto.getDriver());
        mapRideDto.setDuration(distance*2);
    }

    private int getRemainingPointsSize(List<LocationDto> allPoint, MapDriverDto mapDriverDto){
        int index = 0;
        for (int i =0;i<allPoint.size();i++){
            LocationDto point = allPoint.get(i);
            if(point.getLongitude() == mapDriverDto.getLongitude() && point.getLatitude() == mapDriverDto.getLatitude()){
                index = i;
                break;
            }
        }
        return allPoint.size() - index;
    }

    public Page<Ride> findEndedDriversRidesByEmail(Pageable paging, String email) {
        Page<Ride> endedRides = rideRepository.findAllByDriverEmailAndRideStatus(email, RideStatus.ENDED, paging);
        return endedRides;
    }
}
