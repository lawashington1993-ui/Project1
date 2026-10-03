package com.safetynet.alerts.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.AlertData;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import jakarta.annotation.PostConstruct;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class AlertDataService {
    private static final Logger logger = LoggerFactory.getLogger(AlertDataService.class);
    private final ObjectMapper objectMapper;
    private final Path dataPath;
    private AlertData data;

    @Autowired
    public AlertDataService(ObjectMapper objectMapper) { this(objectMapper, Path.of("src/main/resources/data.json")); }

    public AlertDataService(ObjectMapper objectMapper, Path dataPath) {
        this.objectMapper = objectMapper;
        this.dataPath = dataPath;
    }

    // Loads the application dataset from JSON into memory when the service starts.
    @PostConstruct
    public synchronized void load() {
        try {
            data = objectMapper.readValue(dataPath.toFile(), AlertData.class);
            logger.info("Loaded {} people from {}", data.getPersons().size(), dataPath);
        } catch (JacksonException exception) {
            logger.error("Unable to load data file {}", dataPath, exception);
            throw new IllegalStateException("The data file could not be loaded", exception);
        }
    }

    public synchronized AlertData data() { return data; }

    // Persists the in-memory dataset back to the JSON file using a temporary file and atomic move.
    public synchronized void save() {
        Path temporary = dataPath.resolveSibling("data.json.tmp");
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), data);
            Files.move(temporary, dataPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            logger.info("Saved changes to {}", dataPath);
        } catch (JacksonException exception) {
            logger.error("Unable to save data file {}", dataPath, exception);
            throw new IllegalStateException("The data file could not be saved", exception);
        } catch (IOException exception) {
            logger.error("Unable to save data file {}", dataPath, exception);
            throw new IllegalStateException("The data file could not be saved", exception);
        }
    }

    // Returns all people registered at a given address.
    public List<Person> peopleAt(String address) { return data.getPersons().stream().filter(p -> Objects.equals(address, p.getAddress())).toList(); }

    // Returns all people assigned to a given fire station.
    public List<Person> peopleAtStation(String station) {
        List<String> addresses = data.getFirestations().stream().filter(f -> Objects.equals(station, f.getStation())).map(Firestation::getAddress).toList();
        return data.getPersons().stream().filter(p -> addresses.contains(p.getAddress())).toList();
    }

    // Finds a person by first and last name.
    public Optional<Person> person(String first, String last) { return data.getPersons().stream().filter(p -> Objects.equals(first, p.getFirstName()) && Objects.equals(last, p.getLastName())).findFirst(); }

    // Finds a medical record by first and last name.
    public Optional<MedicalRecord> record(String first, String last) { return data.getMedicalrecords().stream().filter(m -> Objects.equals(first, m.getFirstName()) && Objects.equals(last, m.getLastName())).findFirst(); }

    // Adds a new person and persists the updated dataset.
    public synchronized void addPerson(Person person) { data.getPersons().add(person); save(); }

    // Updates an existing person record and saves the dataset when successful.
    public synchronized boolean updatePerson(Person update) {
        Optional<Person> found = person(update.getFirstName(), update.getLastName());
        if (found.isEmpty()) return false;
        Person current = found.get(); current.setAddress(update.getAddress()); current.setCity(update.getCity()); current.setZip(update.getZip()); current.setPhone(update.getPhone()); current.setEmail(update.getEmail()); save(); return true;
    }

    // Removes a person and saves if the deletion was successful.
    public synchronized boolean deletePerson(String first, String last) { boolean removed = data.getPersons().removeIf(p -> Objects.equals(first, p.getFirstName()) && Objects.equals(last, p.getLastName())); if (removed) save(); return removed; }

    // Adds a new fire station mapping and saves the dataset.
    public synchronized void addFirestation(Firestation mapping) { data.getFirestations().add(mapping); save(); }

    // Updates a fire station mapping for the same address and saves the dataset.
    public synchronized boolean updateFirestation(Firestation update) { if (update.getAddress() == null) return false; for (Firestation current : data.getFirestations()) { if (update.getAddress().equals(current.getAddress())) { current.setStation(update.getStation()); save(); return true; } } return false; }

    // Deletes a fire station mapping by address and/or station, then saves if anything was removed.
    public synchronized boolean deleteFirestation(String address, String station) { boolean removed = data.getFirestations().removeIf(f -> (address == null || address.equals(f.getAddress())) && (station == null || station.equals(f.getStation()))); if (removed) save(); return removed; }

    // Adds a new medical record and persists the dataset.
    public synchronized void addRecord(MedicalRecord record) { data.getMedicalrecords().add(record); save(); }

    // Updates an existing medical record and saves when successful.
    public synchronized boolean updateRecord(MedicalRecord update) { Optional<MedicalRecord> found = record(update.getFirstName(), update.getLastName()); if (found.isEmpty()) return false; MedicalRecord current = found.get(); current.setBirthdate(update.getBirthdate()); current.setMedications(update.getMedications()); current.setAllergies(update.getAllergies()); save(); return true; }

    // Removes a medical record by person name and saves if the deletion succeeded.
    public synchronized boolean deleteRecord(String first, String last) { boolean removed = data.getMedicalrecords().removeIf(m -> Objects.equals(first, m.getFirstName()) && Objects.equals(last, m.getLastName())); if (removed) save(); return removed; }
}