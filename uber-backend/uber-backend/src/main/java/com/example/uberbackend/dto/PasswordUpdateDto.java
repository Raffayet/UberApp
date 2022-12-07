package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class PasswordUpdateDto implements Serializable {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
