package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DriveRequest {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private Boolean isReserved;
    private LocalDate reserveDate;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public DriveRequest(Boolean isReserved, LocalDate reserveDate) {
        this.isReserved = isReserved;
        this.reserveDate = reserveDate;
    }
}
