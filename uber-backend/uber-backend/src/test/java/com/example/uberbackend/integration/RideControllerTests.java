package com.example.uberbackend.integration;

import com.example.uberbackend.model.Point;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Transactional
public class RideControllerTests {

    private static final String MAP_URL_PREFIX = "/rides";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    // EndRide - SW-1-2019
    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenEndRide_thenReturnOk() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        String json = TestUtil.json(input);

        mockMvc.perform(put(MAP_URL_PREFIX + "/2").contentType(contentType).content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenEndRide_thenReturnNotAcceptable_DueToRide() throws Exception {

        MvcResult result = mockMvc.perform(put(MAP_URL_PREFIX + "/11232").contentType(contentType))
                .andExpect(status().isNotAcceptable()).andReturn();

        Assertions.assertEquals("Ride has not been found!", result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(authorities = {"DRIVER"})
    public void whenEndRide_thenReturnNotAcceptable_DueToRideNotStarted() throws Exception {

        MvcResult result = mockMvc.perform(put(MAP_URL_PREFIX + "/1").contentType(contentType))
                .andExpect(status().isNotAcceptable()).andReturn();

        Assertions.assertEquals("Ride needs to be started!", result.getResolvedException().getMessage());
    }
}
