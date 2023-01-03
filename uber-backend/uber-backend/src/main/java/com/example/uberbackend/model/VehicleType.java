package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VehicleType {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Double coefficient;

    public VehicleType(String type, Double coefficient) {
        this.type = type;
        this.coefficient = coefficient;
    }
}
