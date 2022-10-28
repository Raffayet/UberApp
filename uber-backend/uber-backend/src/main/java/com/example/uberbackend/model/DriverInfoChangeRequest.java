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
public class DriverInfoChangeRequest {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private Boolean accepted;

    public DriverInfoChangeRequest(Boolean accepted) {
        this.accepted = accepted;
    }
}
