package com.example.uberbackend.integration;

import com.example.uberbackend.dto.LocationDto;
import com.example.uberbackend.dto.MapDriverDto;
import com.example.uberbackend.dto.MapRideDto;
import com.example.uberbackend.dto.PathInfoDto;
import com.example.uberbackend.model.Driver;
import com.example.uberbackend.model.Point;
import com.example.uberbackend.model.enums.RideStatus;
import com.example.uberbackend.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Transactional
public class MapControllerTests {

    private static final String MAP_URL_PREFIX = "/map";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    // GetOptimalRoute - SW-1-2019
    @Test
    public void whenGetOptimalRoute_thenReturnOK() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-optimal-route").contentType(contentType).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInsufficientPoints_whenGetOptimalRoute_thenReturnNotAcceptable() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        MvcResult result = mockMvc.perform(post(MAP_URL_PREFIX + "/determine-optimal-route").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("More locations are needed in order to create route.", result.getResolvedException().getMessage());
    }

    @Test
    public void givenTooManyPoints_whenGetOptimalRoute_thenReturnNotAcceptable() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        MvcResult result = mockMvc.perform(post(MAP_URL_PREFIX + "/determine-optimal-route").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("There is too many locations for route creation.", result.getResolvedException().getMessage());
    }

    @Test
    public void givenInvalidLongitude_whenGetOptimalRoute_thenReturnBadRequest() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(41.2530233, 19333.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-optimal-route").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Invalid value for longitude!")));
    }

    @Test
    public void givenInvalidLatitude_whenGetOptimalRoute_thenReturnBadRequest() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(441.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-optimal-route").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Invalid value for latitude!")));
    }

    // GetCustomRoute SW-1-2019
    @Test
    public void whenGetCustomRoute_thenReturnOK() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-custom-route").contentType(contentType).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInsufficientPoints_whenGetCustomRoute_thenReturnNotAcceptable() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        MvcResult result = mockMvc.perform(post(MAP_URL_PREFIX + "/determine-custom-route").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("More locations are needed in order to create route.", result.getResolvedException().getMessage());
    }

    @Test
    public void givenTooManyPoints_whenGetCustomRoute_thenReturnNotAcceptable() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        MvcResult result = mockMvc.perform(post(MAP_URL_PREFIX + "/determine-custom-route").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("There is too many locations for route creation.", result.getResolvedException().getMessage());
    }

    @Test
    public void givenInvalidLongitude_whenGetCustomRoute_thenReturnBadRequest() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(41.2530233, 19333.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-custom-route").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Invalid value for longitude!")));
    }

    @Test
    public void givenInvalidLatitude_whenGetCustomRoute_thenReturnBadRequest() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(441.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-custom-route").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Invalid value for latitude!")));
    }

    // GetAlternativeRoute SW-1-2019
    @Test
    public void whenGetAlternativeRoute_thenReturnOK() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-alternative-route").contentType(contentType).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void givenInsufficientPoints_whenGetAlternativeRoute_thenReturnNotAcceptable() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        MvcResult result = mockMvc.perform(post(MAP_URL_PREFIX + "/determine-alternative-route").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("More locations are needed in order to create route.", result.getResolvedException().getMessage());

    }

    @Test
    public void givenTooManyPoints_whenGetAlternativeRoute_thenReturnNotAcceptable() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        MvcResult result = mockMvc.perform(post(MAP_URL_PREFIX + "/determine-alternative-route").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("There is too many locations for route creation.", result.getResolvedException().getMessage());
    }

    @Test
    public void givenInvalidLongitude_whenGetAlternativeRoute_thenReturnBadRequest() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(41.2530233, 19333.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-alternative-route").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Invalid value for longitude!")));
    }

    @Test
    public void givenInvalidLatitude_whenGetAlternativeRoute_thenReturnBadRequest() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(441.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2530233, 19.7916443));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-alternative-route").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Invalid value for latitude!")));
    }

    // UpdateDriverOnMap SW-1-2019
    @Test
    public void givenNonexistentDriver_whenUpdateDriverOnMap_thenReturnNotAcceptable() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();

        dto.setId(22L);

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        driver.setId(111L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setStatus(RideStatus.WAITING);
        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        MvcResult result = mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType)
                .content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("Driver has not been found!", result.getResolvedException().getMessage());
    }

    @Test
    public void givenNonexistentRide_whenUpdateDriverOnMap_thenReturnNotAcceptable() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();

        dto.setId(22L);

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        driver.setId(2L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setStatus(RideStatus.WAITING);
        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        MvcResult result = mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType)
                        .content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("Ride has not been found!", result.getResolvedException().getMessage());
    }

    @Test
    public void whenUpdateDriverOnMap_thenReturnOk() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();

        dto.setId(2L);

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        driver.setId(2L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setStatus(RideStatus.WAITING);
        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        MvcResult result = mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType)
                        .content(json))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void givenNoRideId_whenUpdateDriverOnMap_thenReturnBadRequest() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        driver.setId(2L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setStatus(RideStatus.WAITING);
        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Ride id can't be empty!")));
    }

    @Test
    public void givenNoLocations_whenUpdateDriverOnMap_thenReturnBadRequest() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();
        dto.setId(22L);

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        driver.setId(2L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setStatus(RideStatus.WAITING);
        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Information missing!")));
    }

    @Test
    public void givenNoBeforeLocations_whenUpdateDriverOnMap_thenReturnBadRequest() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();
        dto.setId(22L);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        driver.setId(2L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setStatus(RideStatus.WAITING);
        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Information missing!")));
    }

    @Test
    public void givenNoDriver_whenUpdateDriverOnMap_thenReturnBadRequest() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();
        dto.setId(22L);

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        dto.setStatus(RideStatus.WAITING);
        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Driver can't be empty!")));
    }

    @Test
    public void givenNoRideStatus_whenUpdateDriverOnMap_thenReturnBadRequest() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();
        dto.setId(22L);

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        driver.setId(2L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setClientEmails(new ArrayList<>());
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Ride status can't be empty!")));
    }

    @Test
    public void givenNoClientEmails_whenUpdateDriverOnMap_thenReturnBadRequest() throws Exception {
        MapRideDto dto = new MapRideDto();
        MapDriverDto driver = new MapDriverDto();
        dto.setId(22L);

        List<LocationDto> points = new ArrayList<>();
        points.add(new LocationDto(44.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        points.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPoints(points);

        List<LocationDto> pointsBefore = new ArrayList<>();
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        pointsBefore.add(new LocationDto(45.2530233, 19.7916443));
        dto.setAtomicPointsBeforeRide(pointsBefore);

        driver.setId(2L);
        driver.setLongitude(19.78);
        driver.setLongitude(45.89);
        dto.setDriver(driver);

        dto.setStatus(RideStatus.WAITING);
        dto.setDuration(30);

        String json = TestUtil.json(dto);

        mockMvc.perform(put(MAP_URL_PREFIX + "/").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.[*].defaultMessage").value(contains("Client emails can't be empty!")));
    }
}
