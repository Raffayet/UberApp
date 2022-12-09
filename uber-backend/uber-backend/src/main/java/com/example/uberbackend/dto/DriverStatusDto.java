package com.example.uberbackend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatusDto {

    @JsonProperty
    private String email;
    @JsonProperty
    private Boolean active;
}
