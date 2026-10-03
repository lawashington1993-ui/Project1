package com.safetynet.alerts.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FirestationDto;
import com.safetynet.alerts.dto.MedicalRecordDto;
import com.safetynet.alerts.dto.PersonDto;
import com.safetynet.alerts.mapper.AlertMapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.AlertDataService;

import jakarta.validation.Valid;

@RestController
public class CrudController {
    private final AlertDataService dataService;
    private final AlertMapper mapper;
    public CrudController(AlertDataService dataService, AlertMapper mapper) { this.dataService = dataService; this.mapper = mapper; }

    // Creates a new person record in the application dataset.
    @PostMapping("/person") public ResponseEntity<PersonDto> addPerson(@Valid @RequestBody PersonDto dto) { Person person = mapper.toPerson(dto); dataService.addPerson(person); return ResponseEntity.created(URI.create("/person?firstName=" + person.getFirstName() + "&lastName=" + person.getLastName())).header(HttpHeaders.LOCATION, "/person?firstName=" + person.getFirstName() + "&lastName=" + person.getLastName()).body(mapper.toPersonDto(person)); }
    // Updates an existing person using the data sent in the request.
    @PutMapping("/person") public ResponseEntity<?> updatePerson(@Valid @RequestBody PersonDto dto) { Person person = mapper.toPerson(dto); return dataService.updatePerson(person) ? ResponseEntity.ok().header(HttpHeaders.LOCATION, "/person?firstName=" + person.getFirstName() + "&lastName=" + person.getLastName()).body(mapper.toPersonDto(person)) : notFound("Person not found"); }
    // Deletes a person by matching their first and last name.
    @DeleteMapping("/person") public ResponseEntity<?> deletePerson(@RequestParam String firstName, @RequestParam String lastName) { return dataService.deletePerson(firstName, lastName) ? ResponseEntity.noContent().build() : notFound("Person not found"); }

    // Creates a new mapping between an address and a fire station.
    @PostMapping("/firestation") public ResponseEntity<FirestationDto> addFirestation(@Valid @RequestBody FirestationDto dto) { Firestation mapping = mapper.toFirestation(dto); dataService.addFirestation(mapping); return ResponseEntity.created(URI.create("/firestation?stationNumber=" + mapping.getStation())).header(HttpHeaders.LOCATION, "/firestation?stationNumber=" + mapping.getStation()).body(mapper.toFirestationDto(mapping)); }
    // Updates an existing fire station assignment for an address.
    @PutMapping("/firestation") public ResponseEntity<?> updateFirestation(@Valid @RequestBody FirestationDto dto) { Firestation mapping = mapper.toFirestation(dto); return dataService.updateFirestation(mapping) ? ResponseEntity.ok().header(HttpHeaders.LOCATION, "/firestation?stationNumber=" + mapping.getStation()).body(mapper.toFirestationDto(mapping)) : notFound("Mapping not found"); }
    // Deletes a fire station mapping using the address, station number, or both.
    @DeleteMapping("/firestation") public ResponseEntity<?> deleteFirestation(@RequestParam(required = false) String address, @RequestParam(required = false) String station) { return dataService.deleteFirestation(address, station) ? ResponseEntity.noContent().build() : notFound("Mapping not found"); }

    // Creates a new medical record for a person.
    @PostMapping("/medicalRecord") public ResponseEntity<MedicalRecordDto> addRecord(@Valid @RequestBody MedicalRecordDto dto) { MedicalRecord record = mapper.toMedicalRecord(dto); dataService.addRecord(record); return ResponseEntity.created(URI.create("/medicalRecord?firstName=" + record.getFirstName() + "&lastName=" + record.getLastName())).header(HttpHeaders.LOCATION, "/medicalRecord?firstName=" + record.getFirstName() + "&lastName=" + record.getLastName()).body(mapper.toMedicalRecordDto(record)); }
    // Updates an existing medical record with new values.
    @PutMapping("/medicalRecord") public ResponseEntity<?> updateRecord(@Valid @RequestBody MedicalRecordDto dto) { MedicalRecord record = mapper.toMedicalRecord(dto); return dataService.updateRecord(record) ? ResponseEntity.ok().header(HttpHeaders.LOCATION, "/medicalRecord?firstName=" + record.getFirstName() + "&lastName=" + record.getLastName()).body(mapper.toMedicalRecordDto(record)) : notFound("Medical record not found"); }
    // Deletes a medical record by finding the person by name.
    @DeleteMapping("/medicalRecord") public ResponseEntity<?> deleteRecord(@RequestParam String firstName, @RequestParam String lastName) { return dataService.deleteRecord(firstName, lastName) ? ResponseEntity.noContent().build() : notFound("Medical record not found"); }

    // Returns a clean 404 response when the requested entity cannot be found.
    private ResponseEntity<Map<String, String>> notFound(String message) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message)); }
}