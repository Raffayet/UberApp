package com.example.uberbackend.dto;

import com.example.uberbackend.model.Point;
import com.example.uberbackend.model.Ride;
import com.example.uberbackend.model.Route;
import com.example.uberbackend.model.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapRideDto {
    private long rideId;
    private List<LocationDto> routePoints;
    private MapDriverDto driver;
    private RideStatus status;

    public MapRideDto(Ride ride){
        this.rideId = ride.getId();
        this.routePoints = new ArrayList<>();
        Route route = ride.getRoute();
        for (Point point:route.getPoints()) {
            LocationDto locationDto = new LocationDto();
            locationDto.setLatitude(point.getLat());
            locationDto.setLongitude(point.getLng());
            this.routePoints.add(locationDto);
        }
        this.driver = new MapDriverDto(ride.getDriver());
        this.status = ride.getRideStatus();
    }
}
