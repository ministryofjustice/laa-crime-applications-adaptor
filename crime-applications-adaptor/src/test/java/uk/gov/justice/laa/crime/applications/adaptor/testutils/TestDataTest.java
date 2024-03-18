package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

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
