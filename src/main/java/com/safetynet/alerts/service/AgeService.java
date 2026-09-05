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
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public int ageOf(Person person, AlertDataService dataService) {
        return dataService.record(person.getFirstName(), person.getLastName())
                .map(MedicalRecord::getBirthdate)
                .map(this::ageOf)
                .orElse(0);
    }

    public int ageOf(String birthdate) {
        try {
            return Period.between(LocalDate.parse(birthdate, FORMAT), LocalDate.now()).getYears();
        } catch (DateTimeParseException | NullPointerException exception) {
            return 0;
        }
    }
}