package com.safetynet.alerts.mapper;

import org.springframework.stereotype.Component;

import com.safetynet.alerts.dto.FirestationDto;
import com.safetynet.alerts.dto.MedicalRecordDto;
import com.safetynet.alerts.dto.PersonDto;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

@Component
public class AlertMapper {
    // Converts a request payload into the internal Person object used in the app.
    public Person toPerson(PersonDto dto) {
        Person person = new Person();
        person.setFirstName(dto.getFirstName()); person.setLastName(dto.getLastName()); person.setAddress(dto.getAddress());
        person.setCity(dto.getCity()); person.setZip(dto.getZip()); person.setPhone(dto.getPhone()); person.setEmail(dto.getEmail());
        return person;
    }

    // Converts the internal Person object back into the DTO shape returned by the API.
    public PersonDto toPersonDto(Person person) {
        PersonDto dto = new PersonDto();
        dto.setFirstName(person.getFirstName()); dto.setLastName(person.getLastName()); dto.setAddress(person.getAddress());
        dto.setCity(person.getCity()); dto.setZip(person.getZip()); dto.setPhone(person.getPhone()); dto.setEmail(person.getEmail());
        return dto;
    }

    // Converts a fire station DTO into the internal fire station model.
    public Firestation toFirestation(FirestationDto dto) {
        Firestation mapping = new Firestation(); mapping.setAddress(dto.getAddress()); mapping.setStation(dto.getStation()); return mapping;
    }

    // Converts the internal fire station model into a DTO for the API response.
    public FirestationDto toFirestationDto(Firestation mapping) {
        FirestationDto dto = new FirestationDto(); dto.setAddress(mapping.getAddress()); dto.setStation(mapping.getStation()); return dto;
    }

    // Converts a medical record request into the internal model used by the service layer.
    public MedicalRecord toMedicalRecord(MedicalRecordDto dto) {
        MedicalRecord record = new MedicalRecord(); record.setFirstName(dto.getFirstName()); record.setLastName(dto.getLastName());
        record.setBirthdate(dto.getBirthdate()); record.setMedications(dto.getMedications()); record.setAllergies(dto.getAllergies()); return record;
    }

    // Converts the internal medical record model back into the DTO the API returns.
    public MedicalRecordDto toMedicalRecordDto(MedicalRecord record) {
        MedicalRecordDto dto = new MedicalRecordDto(); dto.setFirstName(record.getFirstName()); dto.setLastName(record.getLastName());
        dto.setBirthdate(record.getBirthdate()); dto.setMedications(record.getMedications()); dto.setAllergies(record.getAllergies()); return dto;
    }
}