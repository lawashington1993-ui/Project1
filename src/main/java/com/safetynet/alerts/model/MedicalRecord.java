package com.safetynet.alerts.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class MedicalRecord {
    // First name of the person in the medical record.
    @NotBlank
    private String firstName;

    // Last name of the person in the medical record.
    @NotBlank
    private String lastName;

    // Birth date used to calculate age.
    @NotBlank
    private String birthdate;

    // Current medications for the person.
    private List<String> medications = new ArrayList<>();

    // Known allergies for the person.
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