package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.Saving;

class SavingMapperTest {
  private SavingMapper savingMapper;

  @BeforeEach()
  void setUp() {
    savingMapper = new SavingMapper();
  }

  @ParameterizedTest
  @MethodSource("savingTypeMappingTestData")
  void shouldMapFromCrimeApplySavingTypeToCrimeApplicationsCapitalType(
      Saving.SavingType inputCrimeApplySavingType, CapitalOther.CapitalType expectedCapitalType) {
    CapitalOther.CapitalType actualCapitalType =
        savingMapper.mapSavingsTypeToCapitalType(inputCrimeApplySavingType);

    assertEquals(expectedCapitalType, actualCapitalType);
  }

  private static Stream<Arguments> savingTypeMappingTestData() {
    return Stream.of(
        Arguments.of(Saving.SavingType.CASH_ISA, CapitalOther.CapitalType.CASH_ISA),
        Arguments.of(Saving.SavingType.BANK, CapitalOther.CapitalType.SAVINGS),
        Arguments.of(Saving.SavingType.BUILDING_SOCIETY, CapitalOther.CapitalType.SAVINGS),
        Arguments.of(
            Saving.SavingType.NATIONAL_SAVINGS_OR_POST_OFFICE, CapitalOther.CapitalType.SAVINGS),
        Arguments.of(Saving.SavingType.OTHER, CapitalOther.CapitalType.SAVINGS),
        Arguments.of(null, CapitalOther.CapitalType.SAVINGS));
  }

  @ParameterizedTest
  @MethodSource("ownershipTypeMappingTestData")
  void shouldMapFromCrimeApplyOwnershipTypeToCrimeApplicationsAccountOwner(
      Saving.OwnershipType inputCrimeApplyOwnershipType,
      CapitalOther.AccountOwner expectedAccountOwner) {
    CapitalOther.AccountOwner actualAccountOwner =
        savingMapper.mapSavingOwnershipTypeToAccountOwner(inputCrimeApplyOwnershipType);

    assertEquals(expectedAccountOwner, actualAccountOwner);
  }

  private static Stream<Arguments> ownershipTypeMappingTestData() {
    return Stream.of(
        Arguments.of(Saving.OwnershipType.APPLICANT, CapitalOther.AccountOwner.APPLICANT),
        Arguments.of(Saving.OwnershipType.PARTNER, CapitalOther.AccountOwner.PARTNER),
        Arguments.of(Saving.OwnershipType.APPLICANT_AND_PARTNER, CapitalOther.AccountOwner.JOINT),
        Arguments.of(null, null));
  }
}
