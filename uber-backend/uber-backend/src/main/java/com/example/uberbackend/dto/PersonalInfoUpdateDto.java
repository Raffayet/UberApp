package com.example.uberbackend.dto;

import com.example.uberbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoUpdateDto {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String surname;
    private String role;
    private String city;
    private String phone;

    public PersonalInfoUpdateDto(User u){
        this.email = u.getEmail();
        this.name = u.getName();
        this.surname = u.getSurname();
        this.role = u.getRole().getName();
        this.city = u.getCity();
        this.phone = u.getPhoneNumber();
    }
}
