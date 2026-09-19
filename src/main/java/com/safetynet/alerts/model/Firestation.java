package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;

public class Firestation {
    @NotBlank
    private String address;
    @NotBlank
    private String station;

    public String getAddress() { return address; }
    public void setAddress(String value) { address = value; }
    public String getStation() { return station; }
    public void setStation(String value) { station = value; }
}