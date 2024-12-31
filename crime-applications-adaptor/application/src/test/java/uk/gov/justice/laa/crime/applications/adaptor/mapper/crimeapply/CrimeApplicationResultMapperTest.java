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

  @Test
  void shouldMapNullValuesWithoutException() throws JSONException {
    var expected = nullValueCrimeApplicationResult();
    var actual = mapper.map(nullValueRepOrderState());
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
    expected.setPassportResult(expectedResult);
    expected.setMeansResult(null);

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
        .caseType("SUMMARY ONLY")
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
        .passportStatus("COMPLETED")
        .passportResult("PASS")
        .datePassportCreated(LocalDate.of(2024, 5, 5).atStartOfDay())
        .passportAssessorName("PASSPORT ASSESSOR")
        .iojAppealResult("PASS")
        .iojAppealAssessorName("IOJ APPEAL ASSESSOR")
        .iojAppealDate(LocalDate.of(2024, 5, 5).atStartOfDay())
        .ccRepDecision("Granted - Passed Means Test")
        .build();
  }

  private CrimeApplicationResult expectedCrimeApplicationResult() {
    CrimeApplicationResult result = new CrimeApplicationResult();
    result.setUsn(123455);
    result.setCaseId("case_id");
    result.setMaatRef(56789);
    result.setCaseId("case_id");
    result.setCaseType(CrimeApplicationResult.CaseType.fromValue("SUMMARY ONLY"));
    result.setIojResult("PASS");
    result.setIojReason("IOJ Notes");
    result.setAppCreatedDate(LocalDate.of(2024, 5, 5).atStartOfDay());
    result.setIojAssessorName("IOJ ASSESSOR");
    result.setFundingDecision("GRANTED");
    result.setMeansResult("PASS");
    result.setDateMeansCreated(LocalDate.of(2024, 5, 5).atStartOfDay());
    result.setMeansAssessorName("MEANS ASSESSOR");
    result.setPassportResult("PASS");
    result.setDatePassportCreated(LocalDate.of(2024, 5, 5).atStartOfDay());
    result.setPassportAssessorName("PASSPORT ASSESSOR");
    result.setIojAppealResult("PASS");
    result.setIojAppealAssessorName("IOJ APPEAL ASSESSOR");
    result.setIojAppealDate(LocalDate.of(2024, 5, 5).atStartOfDay());
    result.setCcRepDecision("Granted - Passed Means Test");
    return result;
  }

  private RepOrderState nullValueRepOrderState() {
    return RepOrderState.builder()
        .usn(null)
        .maatRef(null)
        .caseId(null)
        .caseType(null)
        .iojResult(null)
        .iojReason(null)
        .dateAppCreated(null)
        .iojAssessorName(null)
        .fundingDecision(null)
        .meansInitResult(null)
        .meansInitStatus(null)
        .meansFullResult(null)
        .dateMeansCreated(null)
        .meansFullStatus(null)
        .meansAssessorName(null)
        .iojAppealResult(null)
        .iojAppealAssessorName(null)
        .iojAppealDate(null)
        .ccRepDecision(null)
        .build();
  }

  private CrimeApplicationResult nullValueCrimeApplicationResult() {
    CrimeApplicationResult result = new CrimeApplicationResult();
    result.setUsn(null);
    result.setCaseId(null);
    result.setMaatRef(null);
    result.setCaseId(null);
    result.setCaseType(null);
    result.setIojResult(null);
    result.setIojReason(null);
    result.setAppCreatedDate(null);
    result.setIojAssessorName(null);
    result.setFundingDecision(null);
    result.setMeansResult(null);
    result.setDateMeansCreated(null);
    result.setMeansAssessorName(null);
    result.setIojAppealResult(null);
    result.setIojAppealAssessorName(null);
    result.setIojAppealDate(null);
    result.setCcRepDecision(null);
    return result;
  }
}
