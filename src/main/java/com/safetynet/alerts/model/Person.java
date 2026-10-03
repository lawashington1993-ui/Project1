package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;

public class Person {
    // Person's first name.
    @NotBlank
    private String firstName;

    // Person's last name.
    @NotBlank
    private String lastName;

    // Street address where the person lives.
    private String address;

    // City of residence.
    private String city;

    // Postal code or ZIP code.
    private String zip;

    // Contact phone number.
    private String phone;

    // Email address.
    private String email;

    public String getFirstName() { return firstName; }
    public void setFirstName(String value) { firstName = value; }
    public String getLastName() { return lastName; }
    public void setLastName(String value) { lastName = value; }
    public String getAddress() { return address; }
    public void setAddress(String value) { address = value; }
    public String getCity() { return city; }
    public void setCity(String value) { city = value; }
    public String getZip() { return zip; }
    public void setZip(String value) { zip = value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
}