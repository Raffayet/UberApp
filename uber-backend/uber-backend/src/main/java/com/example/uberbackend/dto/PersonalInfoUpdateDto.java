package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoUpdateDto {

    private String email;
    private String name;
    private String surname;
    private String role;
    private String city;
    private String phone;
}
