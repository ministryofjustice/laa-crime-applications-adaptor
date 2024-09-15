package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.RepOrderState;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.model.common.atis.CrimeApplicationResult;

class CrimeApplicationResultMapperTest {

  private CrimeApplicationResultMapper mapper;

  @BeforeEach
  public void setup() {
    mapper = new CrimeApplicationResultMapper();
  }

  @Test
  void shouldMapCrimeApplicationResultWithFullMeansAssessment() throws JSONException {
    var expected = expectedCrimeApplicationResult();
    var actual = mapper.map(getRepOrderState());
    String actualCrimeApplicationResults = JsonUtils.objectToJson(actual);
    String expectedCrimeApplicationResults = JsonUtils.objectToJson(expected);
    JSONAssert.assertEquals(
        expectedCrimeApplicationResults, actualCrimeApplicationResults, JSONCompareMode.STRICT);
  }

  @Test
  void shouldMapCrimeApplicationResultWithInitialMeansAssessment() throws JSONException {
    var expected = expectedCrimeApplicationResult();

    RepOrderState repOrderState = getRepOrderState();
    repOrderState.setMeansFullResult(null);
    repOrderState.setMeansFullStatus(null);
    repOrderState.setMeansInitResult("PASS");

    var actual = mapper.map(repOrderState);

    String actualCrimeApplicationResults = JsonUtils.objectToJson(actual);
    String expectedCrimeApplicationResults = JsonUtils.objectToJson(expected);
    JSONAssert.assertEquals(
        expectedCrimeApplicationResults, actualCrimeApplicationResults, JSONCompareMode.STRICT);
  }

  @ParameterizedTest
  @MethodSource("PassportResultTestData")
  void shouldMapCrimeApplicationResultWithPassportAssessment(
      String actualResult, String expectedResult) throws JSONException {
    var expected = expectedCrimeApplicationResult();
    expected.setMeansAssessorName("PASSPORT ASSESSOR");
    expected.setMeansResult(expectedResult);

    RepOrderState repOrderState = getRepOrderStatePassport(actualResult);

    var actual = mapper.map(repOrderState);

    String actualCrimeApplicationResults = JsonUtils.objectToJson(actual);
    String expectedCrimeApplicationResults = JsonUtils.objectToJson(expected);
    JSONAssert.assertEquals(
        expectedCrimeApplicationResults, actualCrimeApplicationResults, JSONCompareMode.STRICT);
  }

  private RepOrderState getRepOrderStatePassport(String actualResult) {
    RepOrderState repOrderState = getRepOrderState();
    repOrderState.setMeansFullResult(null);
    repOrderState.setMeansFullStatus(null);
    repOrderState.setMeansInitResult(null);
    repOrderState.setMeansInitStatus(null);
    repOrderState.setPassportStatus("COMPLETED");
    repOrderState.setPassportResult(actualResult);
    repOrderState.setPassportAssessorName("PASSPORT ASSESSOR");
    repOrderState.setDatePassportCreated(LocalDate.of(2024, 5, 5).atStartOfDay());
    return repOrderState;
  }

  private static Stream<Arguments> PassportResultTestData() {
    return Stream.of(
        Arguments.of("PASS", "PASS"),
        Arguments.of("TEMP", "PASS"),
        Arguments.of("FAIL CONTINUE", "FAIL"),
        Arguments.of("FAIL", "FAIL"));
  }

  private RepOrderState getRepOrderState() {
    return RepOrderState.builder()
        .usn(123455)
        .maatRef(56789)
        .caseId("case_id")
        .caseType("INDICTABLE")
        .iojResult("PASS")
        .iojReason("IOJ Notes")
        .dateAppCreated(LocalDate.of(2024, 5, 5))
        .iojAssessorName("IOJ ASSESSOR")
        .fundingDecision("GRANTED")
        .meansInitResult("FULL")
        .meansInitStatus("COMPLETED")
        .meansFullResult("PASS")
        .dateMeansCreated(LocalDate.of(2024, 5, 5).atStartOfDay())
        .meansFullStatus("COMPLETED")
        .meansAssessorName("MEANS ASSESSOR")
        .build();
  }

  private CrimeApplicationResult expectedCrimeApplicationResult() {
    CrimeApplicationResult result = new CrimeApplicationResult();
    result.setUsn(123455);
    result.setCaseId("case_id");
    result.setMaatRef(56789);
    result.setCaseId("case_id");
    result.setCaseType(CrimeApplicationResult.CaseType.INDICTABLE);
    result.setIojResult("PASS");
    result.setIojReason("IOJ Notes");
    result.setAppCreatedDate(LocalDate.of(2024, 5, 5).atStartOfDay());
    result.setIojAssessorName("IOJ ASSESSOR");
    result.setFundingDecision("GRANTED");
    result.setMeansResult("PASS");
    result.setDateMeansCreated(LocalDate.of(2024, 5, 5).atStartOfDay());
    result.setMeansAssessorName("MEANS ASSESSOR");
    return result;
  }
}
