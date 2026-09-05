package com.safetynet.alerts.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.AgeService;
import com.safetynet.alerts.service.AlertDataService;

@RestController
@RequestMapping(produces = "application/json")
public class AlertController {
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final AlertDataService dataService;
    private final AgeService ageService;

    public AlertController(AlertDataService dataService, AgeService ageService) { this.dataService = dataService; this.ageService = ageService; }

    @GetMapping("/")
    public Map<String, Object> home() { return Map.of("name", "SafetyNet Alerts API", "status", "running"); }

    @GetMapping("/firestation")
    public ResponseEntity<?> firestation(@RequestParam String stationNumber) {
        List<Person> people = dataService.peopleAtStation(stationNumber);
        if (people.isEmpty()) return empty();
        List<Map<String, String>> result = people.stream().map(p -> Map.of("firstName", p.getFirstName(), "lastName", p.getLastName(), "address", p.getAddress(), "phone", p.getPhone())).toList();
        long children = people.stream().filter(p -> ageService.ageOf(p, dataService) <= 18).count();
        return ResponseEntity.ok(Map.of("people", result, "adultCount", people.size() - children, "childCount", children));
    }

    @GetMapping("/childAlert")
    public ResponseEntity<?> childAlert(@RequestParam String address) {
        List<Person> household = dataService.peopleAt(address);
        List<Person> children = household.stream().filter(p -> ageService.ageOf(p, dataService) <= 18).toList();
        if (children.isEmpty()) return empty();
        return ResponseEntity.ok(children.stream().map(child -> {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("firstName", child.getFirstName()); result.put("lastName", child.getLastName()); result.put("age", ageService.ageOf(child, dataService));
            result.put("householdMembers", household.stream().filter(p -> p != child).map(p -> Map.of("firstName", p.getFirstName(), "lastName", p.getLastName())).toList());
            return result;
        }).toList());
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<?> phoneAlert(@RequestParam String firestation) {
        List<String> phones = dataService.peopleAtStation(firestation).stream().map(Person::getPhone).distinct().toList();
        return phones.isEmpty() ? empty() : ResponseEntity.ok(phones);
    }

    @GetMapping("/fire")
    public ResponseEntity<?> fire(@RequestParam String address) {
        List<Person> people = dataService.peopleAt(address);
        if (people.isEmpty()) return empty();
        String station = dataService.data().getFirestations().stream().filter(f -> address.equals(f.getAddress())).map(f -> f.getStation()).findFirst().orElse(null);
        return ResponseEntity.ok(Map.of("stationNumber", station, "residents", people.stream().map(this::medicalPerson).toList()));
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<?> flood(@RequestParam String stations) {
        Set<String> requested = Arrays.stream(stations.split(",")).map(String::trim).collect(Collectors.toSet());
        List<Person> people = requested.stream().flatMap(s -> dataService.peopleAtStation(s).stream()).distinct().toList();
        if (people.isEmpty()) return empty();
        List<Map<String, Object>> households = people.stream().collect(Collectors.groupingBy(Person::getAddress, LinkedHashMap::new, Collectors.toList())).entrySet().stream().map(entry -> Map.of("address", entry.getKey(), "residents", entry.getValue().stream().map(this::medicalPerson).toList())).toList();
        return ResponseEntity.ok(Map.of("households", households));
    }

    @GetMapping("/personInfo")
    public ResponseEntity<?> personInfo(@RequestParam String lastName) {
        List<Person> people = dataService.data().getPersons().stream().filter(p -> lastName.equals(p.getLastName())).toList();
        if (people.isEmpty()) return empty();
        return ResponseEntity.ok(people.stream().map(p -> { Map<String, Object> result = new LinkedHashMap<>(); result.put("firstName", p.getFirstName()); result.put("lastName", p.getLastName()); result.put("address", p.getAddress()); result.put("age", ageService.ageOf(p, dataService)); result.put("email", p.getEmail()); addMedical(result, p); return result; }).toList());
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<?> communityEmail(@RequestParam String city) {
        List<String> emails = dataService.data().getPersons().stream().filter(p -> city.equals(p.getCity())).map(Person::getEmail).distinct().toList();
        return emails.isEmpty() ? empty() : ResponseEntity.ok(emails);
    }

    private Map<String, Object> medicalPerson(Person person) { Map<String, Object> result = new LinkedHashMap<>(); result.put("firstName", person.getFirstName()); result.put("lastName", person.getLastName()); result.put("phone", person.getPhone()); result.put("age", ageService.ageOf(person, dataService)); addMedical(result, person); return result; }
    private void addMedical(Map<String, Object> result, Person person) { Optional<MedicalRecord> record = dataService.record(person.getFirstName(), person.getLastName()); result.put("medications", record.map(MedicalRecord::getMedications).orElse(List.of())); result.put("allergies", record.map(MedicalRecord::getAllergies).orElse(List.of())); }
    private ResponseEntity<Map<String, Object>> empty() { return ResponseEntity.ok(Map.of()); }
}