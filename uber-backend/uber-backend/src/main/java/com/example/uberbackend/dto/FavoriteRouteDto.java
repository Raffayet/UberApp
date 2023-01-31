package com.example.uberbackend.dto;

import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.FavoriteRoute;
import com.example.uberbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRouteDto {

    @NotNull(message = "Locations are mandatory!")
    @Size(min = 2, message = "There must be at least 2 locations")
    private List<MapSearchResultDto> locations;

    @NotNull(message = "Client email is mandatory!")
    private String clientEmail;

    public FavoriteRouteDto(FavoriteRoute fr) {
        this.locations = fr.getLocations();
        this.clientEmail = fr.getClient().getEmail();
    }
}
