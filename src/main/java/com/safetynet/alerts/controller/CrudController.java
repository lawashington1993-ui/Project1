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

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.AlertDataService;

@RestController
public class CrudController {
    private final AlertDataService dataService;
    public CrudController(AlertDataService dataService) { this.dataService = dataService; }

    @PostMapping("/person") public ResponseEntity<Person> addPerson(@RequestBody Person person) { dataService.addPerson(person); return ResponseEntity.status(HttpStatus.CREATED).body(person); }
    @PutMapping("/person") public ResponseEntity<?> updatePerson(@RequestBody Person person) { return dataService.updatePerson(person) ? ResponseEntity.ok(person) : notFound("Person not found"); }
    @DeleteMapping("/person") public ResponseEntity<?> deletePerson(@RequestParam String firstName, @RequestParam String lastName) { return dataService.deletePerson(firstName, lastName) ? ResponseEntity.noContent().build() : notFound("Person not found"); }

    @PostMapping("/firestation") public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation mapping) { dataService.addFirestation(mapping); return ResponseEntity.status(HttpStatus.CREATED).body(mapping); }
    @PutMapping("/firestation") public ResponseEntity<?> updateFirestation(@RequestBody Firestation mapping) { return dataService.updateFirestation(mapping) ? ResponseEntity.ok(mapping) : notFound("Mapping not found"); }
    @DeleteMapping("/firestation") public ResponseEntity<?> deleteFirestation(@RequestParam(required = false) String address, @RequestParam(required = false) String station) { return dataService.deleteFirestation(address, station) ? ResponseEntity.noContent().build() : notFound("Mapping not found"); }

    @PostMapping("/medicalRecord") public ResponseEntity<MedicalRecord> addRecord(@RequestBody MedicalRecord record) { dataService.addRecord(record); return ResponseEntity.status(HttpStatus.CREATED).body(record); }
    @PutMapping("/medicalRecord") public ResponseEntity<?> updateRecord(@RequestBody MedicalRecord record) { return dataService.updateRecord(record) ? ResponseEntity.ok(record) : notFound("Medical record not found"); }
    @DeleteMapping("/medicalRecord") public ResponseEntity<?> deleteRecord(@RequestParam String firstName, @RequestParam String lastName) { return dataService.deleteRecord(firstName, lastName) ? ResponseEntity.noContent().build() : notFound("Medical record not found"); }

    private ResponseEntity<Map<String, String>> notFound(String message) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message)); }
}