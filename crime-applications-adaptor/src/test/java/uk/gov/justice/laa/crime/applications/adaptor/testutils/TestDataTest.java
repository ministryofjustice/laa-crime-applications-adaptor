package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestDataTest {

    @Test
    void verifyThatGetCrimeApplicationTestDataIsNonNull() {
        assertNotNull(TestData.getCrimeApplication());
    }

    @Test
    void verifyThatGetMaatApplicationTestDataIsNonNull() {
        assertNotNull(TestData.getMaatApplication());
   }
}