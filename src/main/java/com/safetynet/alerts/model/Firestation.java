package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;

public class Firestation {
    // The address covered by this fire station.
    @NotBlank
    private String address;

    // The station number responsible for this address.
    @NotBlank
    private String station;

    public String getAddress() { return address; }
    public void setAddress(String value) { address = value; }
    public String getStation() { return station; }
    public void setStation(String value) { station = value; }
}