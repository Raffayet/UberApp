package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Point {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private Double lat;
    private Double lng;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public Point(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
