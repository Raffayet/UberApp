package com.example.uberbackend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String email;
    private String password;
}
