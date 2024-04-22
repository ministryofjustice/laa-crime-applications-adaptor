package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.CaseDetails;

class CaseTypeMapperTest {

  private CaseTypeMapper caseTypeMapper;

  @BeforeEach
  void setUp() {
    caseTypeMapper = new CaseTypeMapper();
  }

  @ParameterizedTest
  @MethodSource("caseTypeMappingTestData")
  void shouldMapFromCrimeApplyCaseTypeToCrimeApplicationsCaseType(
      uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.CaseDetails
              .CaseType
          inputCrimeApplyCaseType,
      CaseDetails.CaseType expectedCaseType) {
    CaseDetails.CaseType actualCaseType = caseTypeMapper.map(inputCrimeApplyCaseType);

    assertEquals(expectedCaseType, actualCaseType);
  }

  private static Stream<Arguments> caseTypeMappingTestData() {
    return Stream.of(
        Arguments.of(null, null),
        Arguments.of(
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
                .CaseDetails.CaseType.SUMMARY_ONLY,
            CaseDetails.CaseType.SUMMARY_ONLY),
        Arguments.of(
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
                .CaseDetails.CaseType.EITHER_WAY,
            CaseDetails.CaseType.EITHER_WAY),
        Arguments.of(
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
                .CaseDetails.CaseType.INDICTABLE,
            CaseDetails.CaseType.INDICTABLE),
        Arguments.of(
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
                .CaseDetails.CaseType.ALREADY_IN_CROWN_COURT,
            CaseDetails.CaseType.CC_ALREADY),
        Arguments.of(
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
                .CaseDetails.CaseType.COMMITTAL,
            CaseDetails.CaseType.COMMITAL),
        Arguments.of(
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
                .CaseDetails.CaseType.APPEAL_TO_CROWN_COURT,
            CaseDetails.CaseType.APPEAL_CC),
        Arguments.of(
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore
                .CaseDetails.CaseType.APPEAL_TO_CROWN_COURT_WITH_CHANGES,
            CaseDetails.CaseType.APPEAL_CC));
  }
}
