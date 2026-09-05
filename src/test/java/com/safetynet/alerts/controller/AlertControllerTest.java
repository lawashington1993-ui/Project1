package com.safetynet.alerts.controller;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.service.AgeService;
import com.safetynet.alerts.service.AlertDataService;

class AlertControllerTest {
    @TempDir Path tempDir;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        Path file = tempDir.resolve("data.json");
        Files.copy(Path.of("src/main/resources/data.json"), file);
        AlertDataService service = new AlertDataService(new ObjectMapper(), file);
        service.load();
        mockMvc = MockMvcBuilders.standaloneSetup(new AlertController(service, new AgeService()), new CrudController(service)).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void supportsAllReadEndpointsAndEmptyResponses() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("running"));
        mockMvc.perform(get("/firestation?stationNumber=1")).andExpect(status().isOk()).andExpect(jsonPath("$.people").isArray());
        mockMvc.perform(get("/firestation?stationNumber=missing")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/childAlert?address=1509 Culver St")).andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName").exists());
        mockMvc.perform(get("/childAlert?address=missing")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/phoneAlert?firestation=1")).andExpect(jsonPath("$").isArray());
        mockMvc.perform(get("/phoneAlert?firestation=missing")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/fire?address=1509 Culver St")).andExpect(jsonPath("$.residents").isArray());
        mockMvc.perform(get("/fire?address=missing")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/flood/stations?stations=1,2")).andExpect(jsonPath("$.households").isArray());
        mockMvc.perform(get("/flood/stations?stations=missing")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/personInfo?lastName=Boyd")).andExpect(jsonPath("$[0].email").exists());
        mockMvc.perform(get("/personInfo?lastName=missing")).andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/communityEmail?city=Culver")).andExpect(jsonPath("$").isArray());
        mockMvc.perform(get("/communityEmail?city=missing")).andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void supportsCrudEndpoints() throws Exception {
        String person = "{\"firstName\":\"Temp\",\"lastName\":\"Person\",\"address\":\"Temp St\",\"city\":\"Culver\",\"zip\":\"00000\",\"phone\":\"000\",\"email\":\"temp@example.com\"}";
        mockMvc.perform(post("/person").contentType("application/json").content(person)).andExpect(status().isCreated());
        mockMvc.perform(put("/person").contentType("application/json").content(person.replace("Temp St", "New St"))).andExpect(status().isOk());
        mockMvc.perform(delete("/person?firstName=Temp&lastName=Person")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/person?firstName=Missing&lastName=Person")).andExpect(status().isNotFound());
    }
}