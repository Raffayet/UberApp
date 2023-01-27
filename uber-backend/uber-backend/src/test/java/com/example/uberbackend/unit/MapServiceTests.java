package com.example.uberbackend.unit;

import com.example.uberbackend.dto.PathInfoDto;
import com.example.uberbackend.exception.NotEnoughPointsForRouteException;
import com.example.uberbackend.exception.TooManyPointsForRouteException;
import com.example.uberbackend.model.Point;
import com.example.uberbackend.service.MapService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootTest
public class MapServiceTests {

    @InjectMocks
    MapService mapService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(mapService, "graphhopperApiKey", "05c46d80-8250-4308-9cde-cf76b18b1cb4");
    }

    // GetOptimalRoute - SW-1-2019
    @Test
    public void shouldReturnOptimalRouteTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        //output
        List<Point> outputPoints = new ArrayList<>();
        outputPoints.add(new Point(45.252794, 19.791801));
        outputPoints.add(new Point(45.25224, 19.790162));
        outputPoints.add(new Point(45.253148, 19.789592));
        outputPoints.add(new Point(45.253405, 19.789577));
        outputPoints.add(new Point(45.25352, 19.789646));
        outputPoints.add(new Point(45.253703, 19.789866));
        outputPoints.add(new Point(45.255064, 19.793814));
        double distance = 666.185;
        PathInfoDto expectedOutput = new PathInfoDto(outputPoints, distance);

        PathInfoDto result = mapService.getOptimalRoute(input);

        Assertions.assertEquals(expectedOutput, result);
    }

    @Test
    public void shouldThrowNotEnoughPointsForOptimalRouteExceptionTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));

        Assertions.assertThrows(NotEnoughPointsForRouteException.class,
                () -> mapService.getOptimalRoute(input));
    }

    @Test
    public void shouldThrowNotTooManyPointsForOptimalRouteExceptionTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));
        input.add(new Point(45.2550743, 19.8938139));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));
        input.add(new Point(45.2550743, 19.8938139));

        Assertions.assertThrows(TooManyPointsForRouteException.class,
                () -> mapService.getOptimalRoute(input));
    }

    // GetAlternativeRoute - SW-1-2019
    @Test
    public void shouldReturnAlternativeRouteTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        //output
        List<Point> outputPoints = new ArrayList<>();
        outputPoints.add(new Point(45.252794, 19.791801));
        outputPoints.add(new Point(45.253977, 19.795277));
        outputPoints.add(new Point(45.255366, 19.79435));
        outputPoints.add(new Point(45.253577, 19.789149));
        outputPoints.add(new Point(45.253477, 19.789211));
        outputPoints.add(new Point(45.255064, 19.793814));
        double distance = 1351.022;
        PathInfoDto expectedOutput = new PathInfoDto(outputPoints, distance);

        PathInfoDto result = mapService.getAlternativeRoute(input);

        Assertions.assertEquals(expectedOutput, result);
    }

    @Test
    public void shouldThrowNotEnoughPointsForAlternativeRouteExceptionTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));

        Assertions.assertThrows(NotEnoughPointsForRouteException.class,
                () -> mapService.getAlternativeRoute(input));
    }

    @Test
    public void shouldThrowNotTooManyPointsForAlternativeRouteExceptionTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));
        input.add(new Point(45.2550743, 19.8938139));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));
        input.add(new Point(45.2550743, 19.8938139));

        Assertions.assertThrows(TooManyPointsForRouteException.class,
                () -> mapService.getAlternativeRoute(input));
    }

    // GetCustomRoute - SW-1-2019
    @Test
    public void shouldReturnCustomRouteTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        //output
        List<Point> outputPoints = new ArrayList<>();
        outputPoints.add(new Point(45.252794, 19.791801));
        outputPoints.add(new Point(45.25224, 19.790162));
        outputPoints.add(new Point(45.253148, 19.789592));
        outputPoints.add(new Point(45.253405, 19.789577));
        outputPoints.add(new Point(45.25352, 19.789646));
        outputPoints.add(new Point(45.253703, 19.789866));
        outputPoints.add(new Point(45.255064, 19.793814));
        double distance = 666.185;
        PathInfoDto expectedOutput = new PathInfoDto(outputPoints, distance);

        PathInfoDto result = mapService.getCustomRoute(input);

        Assertions.assertEquals(expectedOutput, result);
    }

    @Test
    public void shouldThrowNotEnoughPointsForCustomRouteExceptionTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));

        Assertions.assertThrows(NotEnoughPointsForRouteException.class,
                () -> mapService.getCustomRoute(input));
    }

    @Test
    public void shouldThrowNotTooManyPointsForCustomRouteExceptionTest() throws IOException {
        //input
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));
        input.add(new Point(45.2550743, 19.8938139));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));
        input.add(new Point(45.2550743, 19.8938139));

        Assertions.assertThrows(TooManyPointsForRouteException.class,
                () -> mapService.getCustomRoute(input));
    }
}
