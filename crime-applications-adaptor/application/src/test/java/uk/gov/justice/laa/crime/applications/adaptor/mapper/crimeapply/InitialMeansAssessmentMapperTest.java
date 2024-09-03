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
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.*;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.IncomeDetails;

class InitialMeansAssessmentMapperTest {

  private InitialMeansAssessmentMapper initialMeansAssessmentMapper;

  @BeforeEach
  void setUp() {
    initialMeansAssessmentMapper = new InitialMeansAssessmentMapper();
  }

  @Test
  void shouldSuccessfullyMapCrimeApplyIncomeDetailsToInitialMeansAssessment() throws JSONException {
    IncomeDetails crimeApplyIncomeDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json")
            .getMeansDetails()
            .getIncomeDetails();

    InitialMeansAssessment initialMeansAssessment =
        initialMeansAssessmentMapper.map(crimeApplyIncomeDetails);

    String actualInitialMeansAssessmentJSON = JsonUtils.objectToJson(initialMeansAssessment);
    String expectedInitialMeansAssessmentJSON =
        FileUtils.readFileToString(
            "data/expected/crimeapplication/InitialMeansAssessment_mapped.json");
    JSONAssert.assertEquals(
        expectedInitialMeansAssessmentJSON,
        actualInitialMeansAssessmentJSON,
        JSONCompareMode.STRICT);
  }

  @Test
  void shouldSuccessfullyMapNullCrimeApplyIncomeDetailsToEmptyAdapterInitialMeansAssessment()
      throws JSONException {
    IncomeDetails crimeApplyIncomeDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json")
            .getMeansDetails()
            .getIncomeDetails();

    crimeApplyIncomeDetails.setIncomeBenefits(null);
    crimeApplyIncomeDetails.setIncomePayments(null);
    crimeApplyIncomeDetails.setDependants(null);

    InitialMeansAssessment initialMeansAssessment =
        initialMeansAssessmentMapper.map(crimeApplyIncomeDetails);

    String actualInitialMeansAssessmentJSON = JsonUtils.objectToJson(initialMeansAssessment);
    JSONAssert.assertEquals("{}", actualInitialMeansAssessmentJSON, JSONCompareMode.STRICT);
  }

  @ParameterizedTest
  @MethodSource("withOutIncomeTestData")
  void shouldMapInitialAssessmentNote(String code, String note) {
    IncomeDetails crimeApplyIncomeDetails =
        TestData.getMaatApplication("MaatApplication_unemployed.json")
            .getMeansDetails()
            .getIncomeDetails();
    crimeApplyIncomeDetails.setManageWithoutIncome("other");
    crimeApplyIncomeDetails.setManageOtherDetails("They are living on the streets or homeless");
    InitialMeansAssessment initialMeansAssessment =
        initialMeansAssessmentMapper.map(crimeApplyIncomeDetails);
    assertEquals(
        crimeApplyIncomeDetails.getManageOtherDetails(),
        initialMeansAssessment.getInitialAssessmentNote());
  }

  private static Stream<Arguments> withOutIncomeTestData() {
    return Stream.of(
        Arguments.of("friends_sofa", "They sleep on a friend's sofa for free"),
        Arguments.of("family", "They stay with family for free"),
        Arguments.of("custody", "They have been in custody for more than 3 months"),
        Arguments.of("other", "They are living on the streets or homeless"),
        Arguments.of("living_on_streets", "They are living on the streets or homeless"));
  }
}
