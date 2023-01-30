package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MapSearchResultDto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String displayName;
    private String lat;
    private String lon;

    public MapSearchResultDto(String displayName, String lat, String lon){
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
    }
}
