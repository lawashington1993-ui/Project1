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
// This class is the main data layer for the application.
// It loads the JSON file into memory when the app starts,
// gives the rest of the app a clean way to read and update data,
// and saves the final state back to the file whenever data changes.
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

    // Reads the JSON file and loads it into memory so the app can work with the current data.
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

    // Saves the current in-memory data back to the JSON file.
    // We write to a temporary file first and then replace the original file to reduce the chance of losing data.
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

    // Finds all people who live at a specific address.
    public List<Person> peopleAt(String address) { return data.getPersons().stream().filter(p -> Objects.equals(address, p.getAddress())).toList(); }

    // Finds all people assigned to a specific fire station.
    public List<Person> peopleAtStation(String station) {
        List<String> addresses = data.getFirestations().stream().filter(f -> Objects.equals(station, f.getStation())).map(Firestation::getAddress).toList();
        return data.getPersons().stream().filter(p -> addresses.contains(p.getAddress())).toList();
    }

    // Looks up a single person using their first and last name.
    public Optional<Person> person(String first, String last) { return data.getPersons().stream().filter(p -> Objects.equals(first, p.getFirstName()) && Objects.equals(last, p.getLastName())).findFirst(); }

    // Looks up a medical record using the same person name fields.
    public Optional<MedicalRecord> record(String first, String last) { return data.getMedicalrecords().stream().filter(m -> Objects.equals(first, m.getFirstName()) && Objects.equals(last, m.getLastName())).findFirst(); }

    // Adds a person to the dataset and then saves the latest version to the JSON file.
    public synchronized void addPerson(Person person) { data.getPersons().add(person); save(); }

    // Updates a person if they already exist and writes the changes back to disk.
    public synchronized boolean updatePerson(Person update) {
        Optional<Person> found = person(update.getFirstName(), update.getLastName());
        if (found.isEmpty()) return false;
        Person current = found.get(); current.setAddress(update.getAddress()); current.setCity(update.getCity()); current.setZip(update.getZip()); current.setPhone(update.getPhone()); current.setEmail(update.getEmail()); save(); return true;
    }

    // Removes a person from the dataset and saves the result if the deletion succeeds.
    public synchronized boolean deletePerson(String first, String last) { boolean removed = data.getPersons().removeIf(p -> Objects.equals(first, p.getFirstName()) && Objects.equals(last, p.getLastName())); if (removed) save(); return removed; }

    // Adds a new address-to-fire-station mapping and saves it.
    public synchronized void addFirestation(Firestation mapping) { data.getFirestations().add(mapping); save(); }

    // Updates a fire station mapping for the same address and saves the change.
    public synchronized boolean updateFirestation(Firestation update) { if (update.getAddress() == null) return false; for (Firestation current : data.getFirestations()) { if (update.getAddress().equals(current.getAddress())) { current.setStation(update.getStation()); save(); return true; } } return false; }

    // Deletes a fire station mapping by address and/or station, then saves the file if anything changed.
    public synchronized boolean deleteFirestation(String address, String station) { boolean removed = data.getFirestations().removeIf(f -> (address == null || address.equals(f.getAddress())) && (station == null || station.equals(f.getStation()))); if (removed) save(); return removed; }

    // Adds a medical record and saves the updated dataset.
    public synchronized void addRecord(MedicalRecord record) { data.getMedicalrecords().add(record); save(); }

    // Updates a medical record if it exists and saves the change.
    public synchronized boolean updateRecord(MedicalRecord update) { Optional<MedicalRecord> found = record(update.getFirstName(), update.getLastName()); if (found.isEmpty()) return false; MedicalRecord current = found.get(); current.setBirthdate(update.getBirthdate()); current.setMedications(update.getMedications()); current.setAllergies(update.getAllergies()); save(); return true; }

    // Deletes a medical record by person name and saves the file if the deletion was successful.
    public synchronized boolean deleteRecord(String first, String last) { boolean removed = data.getMedicalrecords().removeIf(m -> Objects.equals(first, m.getFirstName()) && Objects.equals(last, m.getLastName())); if (removed) save(); return removed; }
}