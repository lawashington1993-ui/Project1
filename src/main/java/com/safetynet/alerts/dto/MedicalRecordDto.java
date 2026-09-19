package com.safetynet.alerts.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class MedicalRecordDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String birthdate;
    private List<String> medications = new ArrayList<>();
    private List<String> allergies = new ArrayList<>();

    public String getFirstName() { return firstName; }
    public void setFirstName(String value) { firstName = value; }
    public String getLastName() { return lastName; }
    public void setLastName(String value) { lastName = value; }
    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String value) { birthdate = value; }
    public List<String> getMedications() { return medications; }
    public void setMedications(List<String> value) { medications = value == null ? new ArrayList<>() : value; }
    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> value) { allergies = value == null ? new ArrayList<>() : value; }
}