package com.safetynet.alerts.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class MedicalRecordDto {
    // The patient's first name.
    @NotBlank
    private String firstName;

    // The patient's last name.
    @NotBlank
    private String lastName;

    // The patient's birth date. We use it to calculate age.
    @NotBlank
    private String birthdate;

    // The medications the patient is currently taking.
    private List<String> medications = new ArrayList<>();

    // Allergies recorded for the patient.
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