package com.safetynet.alerts.dto;

import jakarta.validation.constraints.NotBlank;

public class PersonDto {
    // The person's first name. This is often used to find the right record.
    @NotBlank
    private String firstName;

    // The person's last name. It is paired with the first name to identify the person.
    @NotBlank
    private String lastName;

    // The street address where the person lives.
    private String address;

    // The city part of the person's address.
    private String city;

    // The postal or ZIP code for the address.
    private String zip;

    // The phone number to reach the person.
    private String phone;

    // The person's email address.
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