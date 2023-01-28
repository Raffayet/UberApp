package com.example.uberbackend.integration;

import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.enums.RideInviteStatus;
import com.example.uberbackend.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    public void createDriveRequestUsernameNotFoundTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();

        driveRequestDto.setInitiatorEmail("notexistingEmail@gmail.com");
        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isBadRequest());
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
                .andExpect(status().isPaymentRequired());

    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveRequestBadRequestTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();
        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto("Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto("Futoska", "45.11", "19.00")
        );
        driveRequestDto.setInitiatorEmail("sasalukic@gmail.com");
        driveRequestDto.setPeople(new ArrayList<>());
        driveRequestDto.setPrice(-50);
        driveRequestDto.setPricePerPassenger(250);
        driveRequestDto.setVehicleType("Standard");
        driveRequestDto.setRouteType("Custom");
        driveRequestDto.setIsReserved(false);
        driveRequestDto.setTimeOfReservation(LocalDateTime.now());
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.now());
        driveRequestDto.setLocations(locations);

        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.[*].defaultMessage").value(contains("Price can't be negative")));

    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveRequestPriceLowerThanPricePerPassengerTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();
        List<MapSearchResultDto> locations = Arrays.asList(
                new MapSearchResultDto("Rumenacka", "45.11", "19.00"),
                new MapSearchResultDto("Futoska", "45.11", "19.00")
        );
        driveRequestDto.setInitiatorEmail("sasalukic@gmail.com");
        driveRequestDto.setPeople(new ArrayList<>());
        driveRequestDto.setPrice(0);
        driveRequestDto.setPricePerPassenger(250);
        driveRequestDto.setVehicleType("Standard");
        driveRequestDto.setRouteType("Custom");
        driveRequestDto.setIsReserved(false);
        driveRequestDto.setTimeOfReservation(LocalDateTime.now());
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.now());
        driveRequestDto.setLocations(locations);

        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveRequestOneLocationTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();
        List<MapSearchResultDto> locations = List.of(
                new MapSearchResultDto("Rumenacka", "45.11", "19.00")
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

        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveRequestTimeOfReservationExTest() throws Exception {
        DriveRequestDto driveRequestDto = new DriveRequestDto();
        List<MapSearchResultDto> locations = List.of(
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
        driveRequestDto.setTimeOfReservation(LocalDateTime.of(2022,11,2,5,0));
        driveRequestDto.setTimeOfRequestForReservation(LocalDateTime.of(2023,1,2,5,0));
        driveRequestDto.setLocations(locations);

        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveInviteSuccessTest() throws Exception {
        DriveInvitationDto driveInvitationDto = new DriveInvitationDto();
        driveInvitationDto.setFirstLocation("Rumenacka, Novi Sad");
        driveInvitationDto.setDestination("Futoska, Novi Sad");
        driveInvitationDto.setEmailFrom("sasalukic@gmail.com");
        driveInvitationDto.setRideInviteStatus(RideInviteStatus.PENDING);
        driveInvitationDto.setEmailsTo(List.of("milicamatic@gmail.com", "strahinjapavlovic@gmail.com"));
        driveInvitationDto.setPriceToPay(10);

        String json = TestUtil.json(driveInvitationDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-invitation").contentType(contentType).content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void createDriveInviteNoEmailsTest() throws Exception {
        DriveInvitationDto driveInvitationDto = new DriveInvitationDto();
        driveInvitationDto.setFirstLocation("Rumenacka, Novi Sad");
        driveInvitationDto.setDestination("Futoska, Novi Sad");
        driveInvitationDto.setEmailFrom("sasalukic@gmail.com");
        driveInvitationDto.setRideInviteStatus(RideInviteStatus.PENDING);
        driveInvitationDto.setEmailsTo(new ArrayList<>());
        driveInvitationDto.setPriceToPay(10);

        String json = TestUtil.json(driveInvitationDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-invitation").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.[*].defaultMessage").value(contains("Email to list can't be null")));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void findAllRideInvitesSuccessTest() throws Exception {

        mockMvc.perform(get(URL_PREFIX + "/get-ride-invites").contentType(contentType).param("email", "sasalukic@gmail.com")).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*]").value(hasSize(1)))
                .andExpect(jsonPath("$.[*].emailTo").value(hasItem("sasalukic@gmail.com")));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void findAllRideInvitesEmptyTest() throws Exception {

        mockMvc.perform(get(URL_PREFIX + "/get-ride-invites").contentType(contentType).param("email", "notexistingmail@gmail.com")).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$.[*]").value(hasSize(0)));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void findAllRideInvitesNullTest() throws Exception {

        mockMvc.perform(get(URL_PREFIX + "/get-ride-invites").contentType(contentType).param("email", "dejanmatic@gmail.com")).andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*]").value(hasSize(0)));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void refundTokensAfterAcceptingSuccessTest() throws Exception {
        String json = TestUtil.json(1L);
        mockMvc.perform(post(URL_PREFIX + "/refund-tokens-after-accepting").contentType(contentType).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("$").exists())
                .andExpect(content().string("Success!"));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void refundTokensAfterAcceptingRideNotFoundExTest() throws Exception {
        String json = TestUtil.json(-1L);
        mockMvc.perform(post(URL_PREFIX + "/refund-tokens-after-accepting").contentType(contentType).content(json)).andDo(print())
                .andExpect(status().isNotAcceptable());
    }

}
