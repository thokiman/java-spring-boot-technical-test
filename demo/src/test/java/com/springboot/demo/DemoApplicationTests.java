package com.springboot.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class DemoApplicationTests {
    public static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/history")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

        logger.warn("Getting status code for initial database from /api/history : {}",mvcResult.getResponse().getStatus());
    }

}
