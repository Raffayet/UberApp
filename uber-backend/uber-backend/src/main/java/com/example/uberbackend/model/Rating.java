package com.example.uberbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer starNumber;
    private String comment;

    @OneToOne
    private Driver driver;

    @OneToOne
    private Client client;

    public Rating(Integer starNumber, String comment) {
        this.starNumber = starNumber;
        this.comment = comment;
    }
}
