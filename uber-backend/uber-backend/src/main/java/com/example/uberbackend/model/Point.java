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

    private Double x;
    private Double y;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
