package com.safetynet.alerts.model;

import java.util.ArrayList;
import java.util.List;

public class AlertData {
    // All registered people in the dataset.
    private List<Person> persons = new ArrayList<>();

    // Fire station mappings between addresses and stations.
    private List<Firestation> firestations = new ArrayList<>();

    // Medical records associated with people in the dataset.
    private List<MedicalRecord> medicalrecords = new ArrayList<>();

    public List<Person> getPersons() { return persons; }
    public void setPersons(List<Person> value) { persons = value == null ? new ArrayList<>() : value; }
    public List<Firestation> getFirestations() { return firestations; }
    public void setFirestations(List<Firestation> value) { firestations = value == null ? new ArrayList<>() : value; }
    public List<MedicalRecord> getMedicalrecords() { return medicalrecords; }
    public void setMedicalrecords(List<MedicalRecord> value) { medicalrecords = value == null ? new ArrayList<>() : value; }
}