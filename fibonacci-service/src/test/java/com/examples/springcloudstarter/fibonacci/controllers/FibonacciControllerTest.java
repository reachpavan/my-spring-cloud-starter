package com.examples.springcloudstarter.fibonacci.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FibonacciControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getFibonacciSeries_withNoParam_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/fibonacci"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("MISSING_PARAM"));
    }

    @Test
    public void getFibonacciSeries_withUnknownParam_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/fibonacci?unknown=10"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("MISSING_PARAM"));
    }

    @Test
    public void getFibonacciSeries_withCountLowerLimit_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/fibonacci?count=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    public void getFibonacciSeries_withCountUpperLimit_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/fibonacci?count=100001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    public void getFibonacciSeries_withGoodValues() throws Exception {
        mockMvc.perform(get("/fibonacci?count=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.series").isArray())
                .andExpect(jsonPath("$.series", hasSize(5)));
    }
}