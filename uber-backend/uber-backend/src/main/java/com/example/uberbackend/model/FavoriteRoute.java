package com.example.uberbackend.model;

import com.example.uberbackend.dto.MapSearchResultDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FavoriteRoute {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    private List<MapSearchResultDto> locations;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    public boolean equals(FavoriteRoute favoriteRoute) {
        if(favoriteRoute.getLocations().size() != this.getLocations().size())
        {
            return false;
        }

        for(int i = 0; i < favoriteRoute.getLocations().size(); i++)
        {
            if(!favoriteRoute.getLocations().get(i).getDisplayName().equals(this.getLocations().get(i).getDisplayName()))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locations, client);
    }
}
