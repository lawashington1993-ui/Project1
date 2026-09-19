package com.safetynet.alerts.dto;

import jakarta.validation.constraints.NotBlank;

public class FirestationDto {
    @NotBlank
    private String address;
    @NotBlank
    private String station;

    public String getAddress() { return address; }
    public void setAddress(String value) { address = value; }
    public String getStation() { return station; }
    public void setStation(String value) { station = value; }
}