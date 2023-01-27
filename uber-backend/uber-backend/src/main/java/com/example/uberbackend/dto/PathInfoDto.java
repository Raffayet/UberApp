package com.example.uberbackend.dto;

import com.example.uberbackend.model.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathInfoDto {

    private List<Point> atomicPoints;
    private double distance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathInfoDto that = (PathInfoDto) o;

        if(this.atomicPoints.size() != that.getAtomicPoints().size())
            return false;
        for(int i=0; i < this.atomicPoints.size(); i++){
            if(!this.atomicPoints.get(i).equals(that.atomicPoints.get(i)))
                return false;
        }
        return this.distance == that.getDistance();
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicPoints, distance);
    }
}
