package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
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
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.Applicant;
import uk.gov.justice.laa.crime.model.common.crimeapplication.common.EmploymentStatus;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MeansPassport;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Partner.InvolvementInCase;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentType;

class ApplicantMapperTest {

  private ApplicantMapper applicantMapper;

  @BeforeEach
  void setUp() {
    applicantMapper = new ApplicantMapper();
  }

  @Test
  void shouldSuccessfullyMapNullCrimeApplyClientDetailsToEmptyAdapterApplicant()
      throws JSONException {
    MaatApplicationExternal nullMaatApplicationExternal = null;

    Applicant actualApplicant = applicantMapper.map(nullMaatApplicationExternal);

    String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
    JSONAssert.assertEquals("{}", actualApplicantJSON, JSONCompareMode.STRICT);
  }

  @Test
  void shouldSuccessfullyMapNullCrimeApplyApplicantToEmptyAdapterApplicant() throws JSONException {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication("MaatApplication_unemployed.json");

    maatApplicationExternal.getClientDetails().setApplicant(null);

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
    JSONAssert.assertEquals("{}", actualApplicantJSON, JSONCompareMode.STRICT);
  }

  @Test
  void shouldSuccessfullyMapPopulatedCrimeApplyClientDetailsToAdapterApplicant()
      throws JSONException {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication("MaatApplication_unemployed.json");

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
    String expectedApplicantJSON =
        FileUtils.readFileToString("data/expected/crimeapplication/Applicant_mapped.json");
    JSONAssert.assertEquals(expectedApplicantJSON, actualApplicantJSON, JSONCompareMode.STRICT);
  }

  @ParameterizedTest
  @MethodSource("employmentMappingTestData")
  void shouldMapEmploymentTypeToEmploymentStatus(
      String stubMaatDataFileName, EmploymentType empType, EmploymentStatus.Code empCode) {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication(stubMaatDataFileName);
    List<EmploymentType> employmentType = List.of(empType);
    maatApplicationExternal.getMeansDetails().getIncomeDetails().setEmploymentType(employmentType);

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    assertEquals(empCode, actualApplicant.getEmploymentStatus().getCode());
  }

  private static Stream<Arguments> employmentMappingTestData() {
    return Stream.of(
        Arguments.of(
            "MaatApplication_unemployed.json",
            EmploymentType.NOT_WORKING,
            EmploymentStatus.Code.NONPASS),
        Arguments.of(
            "MaatApplication_unemployed.json",
            EmploymentType.EMPLOYED,
            EmploymentStatus.Code.EMPLOY),
        Arguments.of(
            "MaatApplication_unemployed.json",
            EmploymentType.SELF_EMPLOYED,
            EmploymentStatus.Code.SELF));
  }

  @Test
  void shouldMapToPassportedIfMeansPassportIsOnBenefitCheck() {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication("MaatApplication_unemployed.json");
    List<MeansPassport> meansPassport = List.of(MeansPassport.ON_BENEFIT_CHECK);
    maatApplicationExternal.setMeansPassport(meansPassport);

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    assertEquals(EmploymentStatus.Code.PASSPORTED, actualApplicant.getEmploymentStatus().getCode());
  }

  @Test
  void shouldMapContraryInterestIfConflictOfInterestIsYes() {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication("MaatApplication_partner.json");

    maatApplicationExternal.getClientDetails().getPartner().setConflictOfInterest("yes");

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    assertEquals("COCON", actualApplicant.getPartnerContraryInterest().getCode());
  }

  @ParameterizedTest
  @MethodSource("partnerInvolvementInCaseTestData")
  void shouldMapContraryInterestBasedOnPartnerInvolvementInCase(
      String stubMaatDataFileName, InvolvementInCase involvementInCase, String code) {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication(stubMaatDataFileName);

    maatApplicationExternal.getClientDetails().getPartner().setInvolvementInCase(involvementInCase);

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    assertEquals(code, actualApplicant.getPartnerContraryInterest().getCode());
  }

  private static Stream<Arguments> partnerInvolvementInCaseTestData() {
    return Stream.of(
        Arguments.of("MaatApplication_partner.json", InvolvementInCase.VICTIM, "ALLV"),
        Arguments.of("MaatApplication_partner.json", InvolvementInCase.PROSECUTION_WITNESS, "PROW"),
        Arguments.of("MaatApplication_partner.json", InvolvementInCase.NO_INVOLVEMENT, "NOCON"));
  }
}
