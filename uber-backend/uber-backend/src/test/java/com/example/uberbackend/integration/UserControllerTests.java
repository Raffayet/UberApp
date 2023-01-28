package com.example.uberbackend.integration;

import com.example.uberbackend.model.Point;
import com.example.uberbackend.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserControllerTests {

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

    // CheckIfUserIsBlocked - SW-1-2019
    @Test
    public void whenIsUserBlocked_thenReturnOk_False() throws Exception {
        String email = "sasalukic@gmail.com";

        mockMvc.perform(get(USER_URL_PREFIX + "/is-blocked?email=" + email))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void whenIsUserBlocked_thenReturnOk_True() throws Exception {
        String email = "aleksandarmitrovic@gmail.com";

        mockMvc.perform(get(USER_URL_PREFIX + "/is-blocked?email=" + email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void whenIsUserBlocked_thenReturnNotAcceptable() throws Exception {
        String email = "nepostojeci@gmail.com";

        mockMvc.perform(get(USER_URL_PREFIX + "/is-blocked?email=" + email))
                .andExpect(status().isNotAcceptable());
    }
}
