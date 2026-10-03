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

import com.safetynet.alerts.mapper.AlertMapper;
import com.safetynet.alerts.service.AgeService;
import com.safetynet.alerts.service.AlertDataService;

import tools.jackson.databind.ObjectMapper;

class AlertControllerTest {
    @TempDir Path tempDir;
    private MockMvc mockMvc;

    // Each test gets its own copy of the source JSON so one test cannot affect another.
    @BeforeEach
    void setUp() throws Exception {
        Path file = tempDir.resolve("data.json");
        Files.copy(Path.of("src/main/resources/data.json"), file);
        AlertDataService service = new AlertDataService(new ObjectMapper(), file);
        service.load();
        mockMvc = MockMvcBuilders.standaloneSetup(new AlertController(service, new AgeService()), new CrudController(service, new AlertMapper())).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    // Checks that all read endpoints return the expected HTTP status and data shape.
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

    // Confirms the create, update, and delete endpoints behave correctly for the main data objects.
    @Test
    void supportsCrudEndpoints() throws Exception {
        String person = "{\"firstName\":\"Temp\",\"lastName\":\"Person\",\"address\":\"Temp St\",\"city\":\"Culver\",\"zip\":\"00000\",\"phone\":\"000\",\"email\":\"temp@example.com\"}";
        mockMvc.perform(post("/person").contentType("application/json").content(person)).andExpect(status().isCreated());
        mockMvc.perform(put("/person").contentType("application/json").content(person.replace("Temp St", "New St"))).andExpect(status().isOk());
        mockMvc.perform(delete("/person?firstName=Temp&lastName=Person")).andExpect(status().isNoContent());
        mockMvc.perform(delete("/person?firstName=Missing&lastName=Person")).andExpect(status().isNotFound());
    }

    // Verifies that malformed JSON is rejected with a clear 400 response.
    @Test
    void returnsBadRequestForMalformedJson() throws Exception {
        mockMvc.perform(post("/person").contentType("application/json").content("{invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Request body must contain valid JSON"));
    }

    // Verifies that missing required parameters fail with a helpful validation message.
    @Test
    void returnsBadRequestForMissingParameter() throws Exception {
        mockMvc.perform(get("/firestation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing required parameter: stationNumber"));
    }

    // Checks that invalid person payloads fail bean validation and return a 400 response.
    @Test
    void returnsBadRequestForInvalidPerson() throws Exception {
        mockMvc.perform(post("/person").contentType("application/json").content("{\"firstName\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}