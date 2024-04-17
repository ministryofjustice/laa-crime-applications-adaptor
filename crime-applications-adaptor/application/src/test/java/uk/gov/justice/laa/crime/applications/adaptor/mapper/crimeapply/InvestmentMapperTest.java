package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CapitalOther;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.Investment;

class InvestmentMapperTest {

  private InvestmentMapper investmentMapper;

  @BeforeEach()
  void setUp() {
    investmentMapper = new InvestmentMapper();
  }

  @ParameterizedTest
  @MethodSource("investmentTypeMappingTestData")
  void shouldMapFromCrimeApplyInvestmentTypeToCrimeApplicationsCapitalType(
      Investment.InvestmentType inputCrimeApplyInvestmentType,
      CapitalOther.CapitalType expectedCapitalType) {
    CapitalOther.CapitalType actualCapitalType =
        investmentMapper.mapInvestmentTypeToCapitalType(inputCrimeApplyInvestmentType);

    assertEquals(expectedCapitalType, actualCapitalType);
  }

  private static Stream<Arguments> investmentTypeMappingTestData() {
    return Stream.of(
        Arguments.of(Investment.InvestmentType.PEP, CapitalOther.CapitalType.PEPS),
        Arguments.of(Investment.InvestmentType.SHARE, CapitalOther.CapitalType.SHARES),
        Arguments.of(Investment.InvestmentType.STOCK, CapitalOther.CapitalType.STOCKS),
        Arguments.of(Investment.InvestmentType.SHARE_ISA, CapitalOther.CapitalType.SHARE_ISA),
        Arguments.of(Investment.InvestmentType.UNIT_TRUST, CapitalOther.CapitalType.OTHER),
        Arguments.of(null, CapitalOther.CapitalType.INVESTMENT));
  }

  @ParameterizedTest
  @MethodSource("ownershipTypeMappingTestData")
  void shouldMapFromCrimeApplyInvestmentOwnershipTypeToCrimeApplicationsAccountOwner(
      Investment.OwnershipType inputCrimeApplyOwnershipType,
      CapitalOther.AccountOwner expectedAccountOwnerType) {
    CapitalOther.AccountOwner actualAccountOwnerType =
        investmentMapper.mapInvestmentOwnerToAccountOwner(inputCrimeApplyOwnershipType);

    assertEquals(expectedAccountOwnerType, actualAccountOwnerType);
  }

  private static Stream<Arguments> ownershipTypeMappingTestData() {
    return Stream.of(
        Arguments.of(Investment.OwnershipType.PARTNER, CapitalOther.AccountOwner.PARTNER),
        Arguments.of(Investment.OwnershipType.APPLICANT, CapitalOther.AccountOwner.APPLICANT),
        Arguments.of(
            Investment.OwnershipType.APPLICANT_AND_PARTNER, CapitalOther.AccountOwner.JOINT));
  }
}
