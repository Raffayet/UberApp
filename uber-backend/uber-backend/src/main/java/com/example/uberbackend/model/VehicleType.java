package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VehicleType {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String type;
    private Double coefficent;

    public VehicleType(String type, Double coefficent) {
        this.type = type;
        this.coefficent = coefficent;
    }
}
