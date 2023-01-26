package com.example.uberbackend.model;

import com.example.uberbackend.dto.PersonalInfoUpdateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DriverInfoChangeRequest {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Boolean accepted;

    private Boolean reviewed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personalInfoUpdateOldDtoId")
    private PersonalInfoUpdateDto oldData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personalInfoUpdateNewDtoId")
    private PersonalInfoUpdateDto newData;

    public DriverInfoChangeRequest(Boolean accepted) {
        this.accepted = accepted;
    }
}
