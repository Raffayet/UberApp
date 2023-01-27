package com.example.uberbackend.integration;

import com.example.uberbackend.dto.PathInfoDto;
import com.example.uberbackend.model.Point;
import com.example.uberbackend.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
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

    @Test
    public void getOptimalRoute() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-optimal-route").contentType(contentType).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void getCustomRoute() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-custom-route")
                        .contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.atomicPoints", hasSize(7)))
                .andExpect(jsonPath("$.distance").value(666.185));
    }

    @Test
    public void getAlternativeRoute() throws Exception {
        List<Point> input = new ArrayList<>();
        input.add(new Point(45.2530233, 19.7916443));
        input.add(new Point(45.2550643, 19.7938139));

        String json = TestUtil.json(input);

        mockMvc.perform(post(MAP_URL_PREFIX + "/determine-alternative-route")
                        .contentType(contentType).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.atomicPoints", hasSize(6)))
                .andExpect(jsonPath("$.distance").value(1351.022));
    }
}
