package com.safetynet.alerts.dto;

import jakarta.validation.constraints.NotBlank;

public class PersonDto {
    // Person's first name.
    @NotBlank
    private String firstName;

    // Person's last name.
    @NotBlank
    private String lastName;

    // Street address for the person.
    private String address;

    // City where the person lives.
    private String city;

    // Postal code for the person's city.
    private String zip;

    // Contact phone number.
    private String phone;

    // Email address for the person.
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