package com.example.uberbackend.integration;

import com.example.uberbackend.dto.DriveAssignatureDto;
import com.example.uberbackend.dto.DriverRejectionDto;
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

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import static org.hamcrest.Matchers.contains;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Transactional
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
    public void givenNonexistentDriverEmail_whenRejectDrive_thenReturnNotAcceptable() throws Exception {
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
    public void givenNonexistentDriveRequestId_whenRejectDrive_thenReturnNotAcceptable() throws Exception {
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
    public void givenNoDriver_whenRejectDrive_thenReturnBadRequest() throws Exception {
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
    public void givenNoInitiatorEmail_whenRejectDrive_thenReturnBadRequest() throws Exception {
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
    public void givenNoRejectionReason_whenRejectDrive_thenReturnBadRequest() throws Exception {
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
    public void givenNoRequestId_whenRejectDrive_thenReturnBadRequest() throws Exception {
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

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void rejectDriveAfterAcceptingSuccessTest() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(1L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive-after-accepting").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Success!"));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void rejectDriveAfterAcceptingBadEmailExTest() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("notemail");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(1L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive-after-accepting").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].defaultMessage").value(contains("Driver email is not of a correct type")));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void rejectDriveAfterAcceptingNullValueExTest() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive-after-accepting").contentType(contentType).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].defaultMessage").value(contains("Request Id can't be null!")));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void rejectDriveAfterAcceptingNoDriverExTest() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("notadriver@gmail.com");
        dto.setInitiatorEmail("sasalukic@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(1L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive-after-accepting").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void rejectDriveAfterAcceptingNoDriveFoundExTest() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("notaclient@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(-1L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive-after-accepting").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void rejectDriveAfterAcceptingNullExTest() throws Exception {
        DriverRejectionDto dto = new DriverRejectionDto();
        dto.setDriverEmail("dejanmatic@gmail.com");
        dto.setInitiatorEmail("notaclient@gmail.com");
        dto.setReasonForRejection("Not applicable.");
        dto.setRequestId(-1L);

        String json = TestUtil.json(dto);

        mockMvc.perform(post(URL_PREFIX + "/reject-drive-after-accepting").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void assignDriveToDriverTestSuccess() throws Exception {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic@gmail.com");
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");
        driveAssignatureDto.setRequestId(1L);

        String json = TestUtil.json(driveAssignatureDto);
        mockMvc.perform(post(URL_PREFIX + "/assign-drive-to-driver").contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Success!"));
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void assignDriveToDriverTestDriverNotFound() throws Exception {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic333@gmail.com");
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");
        driveAssignatureDto.setRequestId(1L);

        String json = TestUtil.json(driveAssignatureDto);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/assign-drive-to-driver").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("Driver has not been found!", result.getResolvedException().getMessage());

    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void assignDriveToDriverTestDriveRequestNotFound() throws Exception {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic@gmail.com");
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");
        driveAssignatureDto.setRequestId(150L);

        String json = TestUtil.json(driveAssignatureDto);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/assign-drive-to-driver").contentType(contentType).content(json))
                .andExpect(status().isNotAcceptable()).andReturn();
        Assertions.assertEquals("Drive request has not been found!", result.getResolvedException().getMessage());

    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void assignDriveToDriverTestFailureNoId() throws Exception {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic@gmail.com");
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");

        String json = TestUtil.json(driveAssignatureDto);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/assign-drive-to-driver").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void assignDriveToDriverTestNoInitiatorEmail() throws Exception {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setDriverEmail("dejanmatic@gmail.com");
        driveAssignatureDto.setRequestId(150L);

        String json = TestUtil.json(driveAssignatureDto);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/assign-drive-to-driver").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void assignDriveToDriverTestNoDriverEmail() throws Exception {
        //input
        DriveAssignatureDto driveAssignatureDto = new DriveAssignatureDto();
        driveAssignatureDto.setInitiatorEmail("sasalukic@gmail.com");
        driveAssignatureDto.setRequestId(150L);

        String json = TestUtil.json(driveAssignatureDto);
        MvcResult result = mockMvc.perform(post(URL_PREFIX + "/assign-drive-to-driver").contentType(contentType).content(json))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
