package com.safetynet.alerts.model;

import jakarta.validation.constraints.NotBlank;

public class Person {
    // The person's first name.
    @NotBlank
    private String firstName;

    // The person's last name.
    @NotBlank
    private String lastName;

    // The street address where the person lives.
    private String address;

    // The city where the person lives.
    private String city;

    // The ZIP or postal code for the person’s home.
    private String zip;

    // The phone number for the person.
    private String phone;

    // The email address for the person.
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