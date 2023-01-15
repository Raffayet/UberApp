package com.example.uberbackend.dto;

import com.example.uberbackend.model.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapDriverDto {
    private long id;
    private double latitude;
    private double longitude;

    public MapDriverDto(Driver driver){
        this.id = driver.getId();
        this.latitude = driver.getCurrentLocation().getLat();
        this.longitude = driver.getCurrentLocation().getLng();
    }

}
