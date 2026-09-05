package com.safetynet.alerts.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

class AlertDataServiceTest {
    @TempDir Path tempDir;
    private AlertDataService service;

    @BeforeEach
    void setUp() throws Exception {
        Path file = tempDir.resolve("data.json");
        Files.copy(Path.of("src/main/resources/data.json"), file);
        service = new AlertDataService(new ObjectMapper(), file);
        service.load();
    }

    @Test
    void loadsAndQueriesJsonData() {
        assertThat(service.data().getPersons()).hasSize(23);
        assertThat(service.peopleAt("1509 Culver St")).hasSize(5);
        assertThat(service.peopleAtStation("1")).hasSize(6);
        assertThat(service.person("John", "Boyd")).isPresent();
        assertThat(service.record("John", "Boyd")).isPresent();
    }

    @Test
    void persistsPersonCrud() throws Exception {
        Person person = new Person(); person.setFirstName("Temp"); person.setLastName("Person"); person.setAddress("Temp St");
        service.addPerson(person);
        assertThat(service.person("Temp", "Person")).isPresent();
        person.setCity("Culver"); person.setPhone("000");
        assertThat(service.updatePerson(person)).isTrue();
        assertThat(new ObjectMapper().readTree(Files.readString(tempDir.resolve("data.json"))).toString()).contains("Temp St");
        assertThat(service.deletePerson("Temp", "Person")).isTrue();
        assertThat(service.deletePerson("Missing", "Person")).isFalse();
    }

    @Test
    void persistsFirestationCrud() {
        Firestation mapping = new Firestation(); mapping.setAddress("Temp Station"); mapping.setStation("9");
        service.addFirestation(mapping);
        assertThat(service.updateFirestation(mapping)).isTrue();
        assertThat(service.deleteFirestation("Temp Station", "9")).isTrue();
        assertThat(service.updateFirestation(new Firestation())).isFalse();
        assertThat(service.deleteFirestation("Missing", null)).isFalse();
    }

    @Test
    void persistsMedicalRecordCrud() {
        MedicalRecord record = new MedicalRecord(); record.setFirstName("Temp"); record.setLastName("Record"); record.setBirthdate("01/01/2000"); record.setMedications(List.of("test:1mg"));
        service.addRecord(record);
        assertThat(service.updateRecord(record)).isTrue();
        assertThat(service.deleteRecord("Temp", "Record")).isTrue();
        assertThat(service.updateRecord(new MedicalRecord())).isFalse();
        assertThat(service.deleteRecord("Missing", "Record")).isFalse();
    }
}