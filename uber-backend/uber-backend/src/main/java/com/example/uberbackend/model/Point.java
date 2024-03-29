package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Point {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = -90, message = "Invalid value for latitude!")
    @Max(value = 90, message = "Invalid value for latitude!")
    private Double lat;
    @Min(value = -180, message = "Invalid value for longitude!")
    @Max(value = 180, message = "Invalid value for longitude!")
    private Double lng;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public Point(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Point(String lat, String lng) {
        this.lat = Double.parseDouble(lat);
        this.lng = Double.parseDouble(lng);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(lat, point.lat) && Objects.equals(lng, point.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lng, route);
    }
}
