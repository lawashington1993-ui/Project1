package com.safetynet.alerts.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

@Service
public class AgeService {
    // Expected format for birth dates in the application data.
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Looks up the person's medical record and computes their age from the stored birthdate.
    public int ageOf(Person person, AlertDataService dataService) {
        return dataService.record(person.getFirstName(), person.getLastName())
                .map(MedicalRecord::getBirthdate)
                .map(this::ageOf)
                .orElse(0);
    }

    // Returns the current age for a birthdate string, or 0 when the value is invalid or missing.
    public int ageOf(String birthdate) {
        try {
            return Period.between(LocalDate.parse(birthdate, FORMAT), LocalDate.now()).getYears();
        } catch (DateTimeParseException | NullPointerException exception) {
            return 0;
        }
    }
}