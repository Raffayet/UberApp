package com.example.uberbackend.integration;

import com.example.uberbackend.dto.DriverRejectionDto;
import com.example.uberbackend.model.DriveRequest;
import com.example.uberbackend.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.contains;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class DriverControllerTests {

    private static final String URL_PREFIX = "/driver";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    // RejectDrive - SW-1-2019
    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenRejectDrive_thenReturnNotAcceptable_DueToDriver() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("nepostojeci@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(123L);

        String json = TestUtil.json(dto);

        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/reject-drive").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();

        Assertions.assertEquals("Driver has not been found!", result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenRejectDrive_thenReturnNotAcceptable_DueToDriveRequest() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(123L);

        String json = TestUtil.json(dto);

        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/reject-drive").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();

        Assertions.assertEquals("Drive request has not been found!", result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenRejectDrive_thenReturnBadRequest_DueToDriverEmail() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(123L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].defaultMessage").value(contains("Driver email can't be null!")));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenRejectDrive_thenReturnBadRequest_DueToInitiatorEmail() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(123L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].defaultMessage").value(contains("Initiator email can't be null!")));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenRejectDrive_thenReturnBadRequest_DueToReason() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setRequestId(123L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].defaultMessage").value(contains("Reason for rejection can't be null!")));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenRejectDrive_thenReturnBadRequest_DueToRequestId() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].defaultMessage").value(contains("Request Id can't be null!")));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenRejectDrive_thenReturnOK() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(1L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive").contentType(contentType).content(json))
                .andExpect(status().isOk());

    }
}
