package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class AgeServiceTest {
    private final AgeService service = new AgeService();

    @Test
    void returnsAgeForValidBirthdate() { assertThat(service.ageOf("03/06/1984")).isGreaterThan(40); }

    @Test
    void returnsZeroForInvalidBirthdate() { assertThat(service.ageOf("invalid")).isZero(); assertThat(service.ageOf(null)).isZero(); }
}