package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.NationalSavingsCertificate;

class NationalSavingsCertificateMapperTest {
  private NationalSavingsCertificateMapper nationalSavingsCertificateMapper;

  @BeforeEach()
  void setUp() {
    nationalSavingsCertificateMapper = new NationalSavingsCertificateMapper();
  }

  @ParameterizedTest
  @MethodSource("ownershipTypeMappingTestData")
  void shouldMapFromCrimeApplyOwnershipTypeToCrimeApplicationsAccountOwner(
      NationalSavingsCertificate.OwnershipType inputCrimeApplyOwnershipType,
      CapitalOther.AccountOwner expectedAccountOwnerType) {
    CapitalOther.AccountOwner actualAccountOwnerType =
        nationalSavingsCertificateMapper.mapOwnershipTypeToAccountOwner(
            inputCrimeApplyOwnershipType);

    assertEquals(expectedAccountOwnerType, actualAccountOwnerType);
  }

  private static Stream<Arguments> ownershipTypeMappingTestData() {
    return Stream.of(
        Arguments.of(
            NationalSavingsCertificate.OwnershipType.APPLICANT,
            CapitalOther.AccountOwner.APPLICANT),
        Arguments.of(
            NationalSavingsCertificate.OwnershipType.PARTNER, CapitalOther.AccountOwner.PARTNER),
        Arguments.of(null, null));
  }
}
