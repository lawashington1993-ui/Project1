package com.safetynet.alerts.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.safetynet.alerts.dto.FirestationDto;
import com.safetynet.alerts.dto.MedicalRecordDto;
import com.safetynet.alerts.dto.PersonDto;
import com.safetynet.alerts.model.Person;

class AlertMapperTest {
    private final AlertMapper mapper = new AlertMapper();

    // Verifies person DTOs are mapped to the model and back without losing address data.
    @Test
    void mapsPersonBothDirections() {
        PersonDto dto = new PersonDto(); dto.setFirstName("Jane"); dto.setLastName("Doe"); dto.setAddress("Main St");
        Person person = mapper.toPerson(dto);
        assertThat(mapper.toPersonDto(person).getAddress()).isEqualTo("Main St");
    }

    // Verifies fire station DTOs round-trip correctly through the model layer.
    @Test
    void mapsFirestationBothDirections() {
        FirestationDto dto = new FirestationDto(); dto.setAddress("Main St"); dto.setStation("1");
        assertThat(mapper.toFirestationDto(mapper.toFirestation(dto)).getStation()).isEqualTo("1");
    }

    // Verifies medical record DTOs preserve the birthdate across both mapping directions.
    @Test
    void mapsMedicalRecordBothDirections() {
        MedicalRecordDto dto = new MedicalRecordDto(); dto.setFirstName("Jane"); dto.setLastName("Doe"); dto.setBirthdate("01/01/2000");
        assertThat(mapper.toMedicalRecordDto(mapper.toMedicalRecord(dto)).getBirthdate()).isEqualTo("01/01/2000");
    }
}