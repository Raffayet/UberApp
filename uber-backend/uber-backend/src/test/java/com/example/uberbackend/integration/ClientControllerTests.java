package com.example.uberbackend.integration;

import com.example.uberbackend.dto.DriveRequestDto;
import com.example.uberbackend.dto.LoginDto;
import com.example.uberbackend.dto.MapSearchResultDto;
import com.example.uberbackend.dto.UserDrivingStatus;
import com.example.uberbackend.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ClientControllerTests {

    private static final String URL_PREFIX = "/client";
    private static final String AUTH_URL_PREFIX = "/auth";
    private static final String USER_URL_PREFIX = "/user";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveRequestDriverNotFoundTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();
        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto("Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto("Futoska", "45.11", "19.00")
        );

        driveRequestDto.setInitiatorEmail("sasalukic@gmail.com");
        driveRequestDto.setPeople(new ArrayList<>());
        driveRequestDto.setPrice(5);
        driveRequestDto.setPricePerPassenger(5);
        driveRequestDto.setVehicleType("Standard");
        driveRequestDto.setRouteType("Custom");
        driveRequestDto.setIsReserved(false);
        driveRequestDto.setTimeOfReservation(LocalDateTime.now());
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.now());
        driveRequestDto.setLocations(locations);

        UserDrivingStatus uds = new UserDrivingStatus();
        uds.setEmail("dejanmatic@gmail.com");
        uds.setStatus(1);
        String logoutJson = TestUtil.json(uds);
        mockMvc.perform(post(USER_URL_PREFIX+"/change-user-driving-status").contentType(contentType).content(logoutJson));

        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveRequestSuccessTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();

        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto("Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto("Futoska", "45.11", "19.00")
        );

        driveRequestDto.setInitiatorEmail("sasalukic@gmail.com");
        driveRequestDto.setPeople(new ArrayList<>());
        driveRequestDto.setPrice(5);
        driveRequestDto.setPricePerPassenger(5);
        driveRequestDto.setVehicleType("Standard");
        driveRequestDto.setRouteType("Custom");
        driveRequestDto.setIsReserved(false);
        driveRequestDto.setTimeOfReservation(LocalDateTime.now());
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.now());
        driveRequestDto.setLocations(locations);

        String json = TestUtil.json(driveRequestDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("dejanmatic@gmail.com");
        loginDto.setPassword("sasa123");
        String loginJson = TestUtil.json(loginDto);

        mockMvc.perform(post(AUTH_URL_PREFIX+"/login").contentType(contentType).content(loginJson));


        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
            .andExpect(status().isOk())
            .andExpect(content().string("Success!"));
    }



    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveRequestNotEnoughTokensTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();
        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto("Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto("Futoska", "45.11", "19.00")
        );

        driveRequestDto.setInitiatorEmail("sasalukic@gmail.com");
        driveRequestDto.setPeople(new ArrayList<>());
        driveRequestDto.setPrice(250);
        driveRequestDto.setPricePerPassenger(250);
        driveRequestDto.setVehicleType("Standard");
        driveRequestDto.setRouteType("Custom");
        driveRequestDto.setIsReserved(false);
        driveRequestDto.setTimeOfReservation(LocalDateTime.now());
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.now());
        driveRequestDto.setLocations(locations);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("dejanmatic@gmail.com");
        loginDto.setPassword("sasa123");
        String loginJson = TestUtil.json(loginDto);
        mockMvc.perform(post(AUTH_URL_PREFIX+"/login").contentType(contentType).content(loginJson));

        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isBadRequest());
    }

}
