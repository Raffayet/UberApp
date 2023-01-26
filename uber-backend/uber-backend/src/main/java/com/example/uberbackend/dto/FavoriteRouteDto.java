package com.example.uberbackend.dto;

import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRouteDto {
    private List<MapSearchResultDto> locations;
    private String clientEmail;
}
