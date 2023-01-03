package com.example.uberbackend.dto;

import com.example.uberbackend.model.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathInfoDto {

    private List<Point> points;
    private double distance;
}
