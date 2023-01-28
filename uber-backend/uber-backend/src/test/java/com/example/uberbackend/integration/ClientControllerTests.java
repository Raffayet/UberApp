package com.example.uberbackend.integration;

import com.example.uberbackend.dto.*;
import com.example.uberbackend.model.Client;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.model.RideInvite;
import com.example.uberbackend.model.enums.RideInviteStatus;
import com.example.uberbackend.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("dejanmatic@gmail.com");
        loginDto.setPassword("sasa123");
        String loginJson = TestUtil.json(loginDto);
        mockMvc.perform(post(AUTH_URL_PREFIX+"/login").contentType(contentType).content(loginJson));

        String json = TestUtil.json(driveRequestDto);
        mockMvc.perform(post(URL_PREFIX + "/create-drive-request").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.[*].defaultMessage").value(contains("Price can't be negative")));

    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void getTokensByEmailTestSuccess() throws Exception {
        //input
        String email = "sasalukic@gmail.com";
        double expectedAmount = 5.0;

        String json = TestUtil.json(email);
        //Act
        mockMvc.perform(get(URL_PREFIX + "/get-tokens?email=" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedAmount));

    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void getTokensByEmailTestEmptyEmail() throws Exception {
        //input
        String email = "";
        double expectedAmount = 10.0;

        String json = TestUtil.json(email);
        //Act
        mockMvc.perform(get(URL_PREFIX + "/get-tokens?email=" + email))
                .andExpect(status().isBadRequest()).andExpect(content().string("Empty string!"));;
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void invitedHasTokensTestSuccessOnlyInitiator() throws Exception {
        //input
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{});
        checkForEnoughTokens.setPricePerPassenger(5);

        //Act

        String json = TestUtil.json(checkForEnoughTokens);
        mockMvc.perform(post(URL_PREFIX + "/invited-has-money").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void invitedHasTokensTestSuccessInvitedPeopleExist() throws Exception {
        //input
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{
                "milicamatic@gmail.com",
                "strahinjapavlovic@gmail.com"});
        checkForEnoughTokens.setPricePerPassenger(5);

        //Act

        String json = TestUtil.json(checkForEnoughTokens);
        mockMvc.perform(post(URL_PREFIX + "/invited-has-money").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void invitedHasTokensTestFailureNegativePrice() throws Exception {
        //input
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{});
        checkForEnoughTokens.setPricePerPassenger(-5);

        //Act

        String json = TestUtil.json(checkForEnoughTokens);
        mockMvc.perform(post(URL_PREFIX + "/invited-has-money").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].defaultMessage").value(contains("Price can't be negative")));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void invitedHasTokensTestFailureNotEnoughTokens() throws Exception {
        //input
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{
                "milicamatic@gmail.com"
        });
        checkForEnoughTokens.setPricePerPassenger(55);

        //Act

        String json = TestUtil.json(checkForEnoughTokens);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/invited-has-money").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("You don't have enough tokens!", result.getResolvedException().getMessage());

    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void invitedHasTokensTestFailureNotExistingEmail() throws Exception {
        //input
        CheckForEnoughTokens checkForEnoughTokens = new CheckForEnoughTokens();
        checkForEnoughTokens.setInitiatorEmail("sasalukic@gmail.com");
        checkForEnoughTokens.setPeopleEmails(new String[]{
                "milicamatic333@gmail.com"
        });
        checkForEnoughTokens.setPricePerPassenger(4);

        //Act

        String json = TestUtil.json(checkForEnoughTokens);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/invited-has-money").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("User has not been found!", result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void changeDriveInvitationStatusTestSuccessAccepted() throws Exception {
        //input
        InvitationStatusDto invitationStatusDto = new InvitationStatusDto();
        invitationStatusDto.setInvitationId(1L);
        invitationStatusDto.setAccepted(true);

        //act
        String json = TestUtil.json(invitationStatusDto);
        mockMvc.perform(put(URL_PREFIX + "/change-drive-invitation-status").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accepted").value(invitationStatusDto.isAccepted()));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void changeDriveInvitationStatusTestSuccessRejected() throws Exception {
        //input
        InvitationStatusDto invitationStatusDto = new InvitationStatusDto();
        invitationStatusDto.setInvitationId(1L);
        invitationStatusDto.setAccepted(false);

        //act
        String json = TestUtil.json(invitationStatusDto);
        mockMvc.perform(put(URL_PREFIX + "/change-drive-invitation-status").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accepted").value(invitationStatusDto.isAccepted()));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void changeDriveInvitationStatusTestFailureNoId() throws Exception {
        //input
        InvitationStatusDto invitationStatusDto = new InvitationStatusDto();
        invitationStatusDto.setAccepted(false);

        //act
        String json = TestUtil.json(invitationStatusDto);
        mockMvc.perform(put(URL_PREFIX + "/change-drive-invitation-status").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].defaultMessage").value("Invitation id does not exist!"));;
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void changeDriveInvitationStatusTestRideInviteNotFound() throws Exception
    {
        //input
        InvitationStatusDto invitationStatusDto = new InvitationStatusDto();
        invitationStatusDto.setInvitationId(150L);
        invitationStatusDto.setAccepted(false);

        //act
        String json = TestUtil.json(invitationStatusDto);
        MvcResult result = mockMvc.perform(put(URL_PREFIX + "/change-drive-invitation-status").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("Ride invite has not been found!", result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void refundTokensTestSuccessNoInvitedPeople() throws Exception {
        //input
        Long requestId = 2L;
        String json = TestUtil.json(requestId);
        mockMvc.perform(post(URL_PREFIX + "/refund-tokens").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Success!"));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void refundTokensTestSuccessInvitedPeople() throws Exception {
        //input
        Long requestId = 1L;
        String json = TestUtil.json(requestId);
        mockMvc.perform(post(URL_PREFIX + "/refund-tokens").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Success!"));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void refundTokensTestDriveRequestNotFound() throws Exception {
        //input
        Long requestId = 155L;
        String json = TestUtil.json(requestId);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/refund-tokens").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("Drive request has not been found!", result.getResolvedException().getMessage());
    }
}
