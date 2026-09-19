package com.safetynet.alerts.controller;

import java.util.Map;

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

    @PostMapping("/person") public ResponseEntity<PersonDto> addPerson(@Valid @RequestBody PersonDto dto) { Person person = mapper.toPerson(dto); dataService.addPerson(person); return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toPersonDto(person)); }
    @PutMapping("/person") public ResponseEntity<?> updatePerson(@Valid @RequestBody PersonDto dto) { Person person = mapper.toPerson(dto); return dataService.updatePerson(person) ? ResponseEntity.ok(mapper.toPersonDto(person)) : notFound("Person not found"); }
    @DeleteMapping("/person") public ResponseEntity<?> deletePerson(@RequestParam String firstName, @RequestParam String lastName) { return dataService.deletePerson(firstName, lastName) ? ResponseEntity.noContent().build() : notFound("Person not found"); }

    @PostMapping("/firestation") public ResponseEntity<FirestationDto> addFirestation(@Valid @RequestBody FirestationDto dto) { Firestation mapping = mapper.toFirestation(dto); dataService.addFirestation(mapping); return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toFirestationDto(mapping)); }
    @PutMapping("/firestation") public ResponseEntity<?> updateFirestation(@Valid @RequestBody FirestationDto dto) { Firestation mapping = mapper.toFirestation(dto); return dataService.updateFirestation(mapping) ? ResponseEntity.ok(mapper.toFirestationDto(mapping)) : notFound("Mapping not found"); }
    @DeleteMapping("/firestation") public ResponseEntity<?> deleteFirestation(@RequestParam(required = false) String address, @RequestParam(required = false) String station) { return dataService.deleteFirestation(address, station) ? ResponseEntity.noContent().build() : notFound("Mapping not found"); }

    @PostMapping("/medicalRecord") public ResponseEntity<MedicalRecordDto> addRecord(@Valid @RequestBody MedicalRecordDto dto) { MedicalRecord record = mapper.toMedicalRecord(dto); dataService.addRecord(record); return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toMedicalRecordDto(record)); }
    @PutMapping("/medicalRecord") public ResponseEntity<?> updateRecord(@Valid @RequestBody MedicalRecordDto dto) { MedicalRecord record = mapper.toMedicalRecord(dto); return dataService.updateRecord(record) ? ResponseEntity.ok(mapper.toMedicalRecordDto(record)) : notFound("Medical record not found"); }
    @DeleteMapping("/medicalRecord") public ResponseEntity<?> deleteRecord(@RequestParam String firstName, @RequestParam String lastName) { return dataService.deleteRecord(firstName, lastName) ? ResponseEntity.noContent().build() : notFound("Medical record not found"); }

    private ResponseEntity<Map<String, String>> notFound(String message) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message)); }
}