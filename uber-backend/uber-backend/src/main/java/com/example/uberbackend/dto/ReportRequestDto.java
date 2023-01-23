package com.example.uberbackend.dto;

import com.example.uberbackend.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {
    private LocalDate start;
    private LocalDate end;
    private String userEmail;
    private RoleType roleType;
}
