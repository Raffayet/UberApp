package com.example.uberbackend.model;

import com.example.uberbackend.dto.MapSearchResultDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FavoriteRoute {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<MapSearchResultDto> locations;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
