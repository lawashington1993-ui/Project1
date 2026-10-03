package com.safetynet.alerts.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class AgeServiceTest {
    private final AgeService service = new AgeService();

    // Confirms that a correctly formatted birth date produces a realistic age.
    @Test
    void returnsAgeForValidBirthdate() { assertThat(service.ageOf("03/06/1984")).isGreaterThan(40); }

    // Confirms that bad or missing dates are handled safely and return zero.
    @Test
    void returnsZeroForInvalidBirthdate() { assertThat(service.ageOf("invalid")).isZero(); assertThat(service.ageOf(null)).isZero(); }
}