package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer startNumber;
    private String comment;

    @OneToOne
    private Driver driver;

    @OneToOne
    private Client client;

    public Rating(Integer startNumber, String comment) {
        this.startNumber = startNumber;
        this.comment = comment;
    }
}
