package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.CapitalEquity;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant.ResidenceType;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;

class CapitalEquityMapperTest {

  private CapitalEquityMapper capitalEquityMapper;

  @BeforeEach
  void setUp() {
    capitalEquityMapper = new CapitalEquityMapper();
  }

  @Test
  void shouldSuccessfullyMapCrimeApplyCapitalDetailsToAdapterCapitalDetails() throws JSONException {
    MaatApplicationExternal crimeApplyWithCapitalDetails =
        TestData.getMaatApplicationWithCapitalDetails();

    CapitalEquity actualCaseDetails = capitalEquityMapper.map(crimeApplyWithCapitalDetails);

    String actualCaseDetailsJSON = JsonUtils.objectToJson(actualCaseDetails);
    String expectedCrimeApplicationJson =
        FileUtils.readFileToString(
            "data/expected/crimeapplication/CrimeApplication_Capital_10000331.json");
    JSONAssert.assertEquals(
        expectedCrimeApplicationJson, actualCaseDetailsJSON, JSONCompareMode.STRICT);
  }

  @ParameterizedTest
  @MethodSource("residentialStatusTestData")
  void shouldSuccessfullyMapCrimeApplyCapitalDetailsWithResidentialStatusToAdapterCapitalDetails(
      Applicant.ResidenceType residentialStatus, String residentialStatusCode) {
    MaatApplicationExternal crimeApplyWithCapitalDetails =
        TestData.getMaatApplicationWithCapitalDetails();
    crimeApplyWithCapitalDetails
        .getClientDetails()
        .getApplicant()
        .setResidenceType(residentialStatus);

    CapitalEquity actualCaseDetails = capitalEquityMapper.map(crimeApplyWithCapitalDetails);

    assertEquals(residentialStatusCode, actualCaseDetails.getResidentialStatus().value());
  }

  private static Stream<Arguments> residentialStatusTestData() {
    return Stream.of(
        Arguments.of(Applicant.ResidenceType.RENTED, "TENANT"),
        Arguments.of(Applicant.ResidenceType.TEMPORARY, "TEMP"),
        Arguments.of(ResidenceType.SOMEONE_ELSE, "TEMP"),
        Arguments.of(Applicant.ResidenceType.PARENTS, "PARENTS"),
        Arguments.of(ResidenceType.PARTNER_OWNED, "OWNER"),
        Arguments.of(ResidenceType.JOINT_OWNED, "OWNER"));
  }
}
