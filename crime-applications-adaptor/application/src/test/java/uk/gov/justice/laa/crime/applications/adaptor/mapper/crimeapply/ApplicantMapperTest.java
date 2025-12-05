package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.*;

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
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.ClientDetails;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MeansPassport;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Partner;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Partner.InvolvementInCase;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentType;

class ApplicantMapperTest {

  private ApplicantMapper applicantMapper;

  @BeforeEach
  void setUp() {
    applicantMapper = new ApplicantMapper();
  }

  // --- map(Partner.DwpResponse) ---

  @Test
  void mapPartnerEnum_whenSourceIsNull_returnsNull() {
    Applicant.PartnerDwpResponse result = applicantMapper.map((Partner.DwpResponse) null);
    assertNull(result);
  }

  @Test
  void mapPartnerEnum_whenSourceIsValid_mapsCorrectly() {
    Partner.DwpResponse source = Partner.DwpResponse.YES; // "Yes"
    Applicant.PartnerDwpResponse result = applicantMapper.map(source);

    assertNotNull(result);
    assertEquals(Applicant.PartnerDwpResponse.YES, result);
    assertEquals(source.value(), result.value()); // both "Yes"
  }

  // --- mapPartnerDwpResponse(MaatApplicationExternal) ---

  @Test
  void mapPartnerDwpResponse_whenPartnerIsNull_returnsNull() {
    MaatApplicationExternal external = new MaatApplicationExternal();
    ClientDetails clientDetails = new ClientDetails();
    clientDetails.setPartner(null);
    external.setClientDetails(clientDetails);

    Applicant.PartnerDwpResponse result = applicantMapper.mapPartnerDwpResponse(external);
    assertNull(result);
  }

  @Test
  void mapPartnerDwpResponse_whenPartnerDwpResponseIsNull_returnsNull() {
    MaatApplicationExternal external = new MaatApplicationExternal();
    ClientDetails clientDetails = new ClientDetails();
    Partner partner = new Partner();
    partner.setDwpResponse(null);
    clientDetails.setPartner(partner);
    external.setClientDetails(clientDetails);

    Applicant.PartnerDwpResponse result = applicantMapper.mapPartnerDwpResponse(external);
    assertNull(result);
  }

  @Test
  void mapPartnerDwpResponse_whenPartnerDwpResponseIsValid_mapsCorrectly() {
    MaatApplicationExternal external = new MaatApplicationExternal();
    ClientDetails clientDetails = new ClientDetails();
    Partner partner = new Partner();

    partner.setDwpResponse(Partner.DwpResponse.UNDETERMINED); // "Undetermined"
    clientDetails.setPartner(partner);
    external.setClientDetails(clientDetails);

    Applicant.PartnerDwpResponse result = applicantMapper.mapPartnerDwpResponse(external);

    assertNotNull(result);
    assertEquals(Applicant.PartnerDwpResponse.UNDETERMINED, result);
    assertEquals("Undetermined", result.value());
  }

  // ---
  // map(uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant.DwpResponse)
  // ---

  @Test
  void mapApplicantEnum_whenSourceIsNull_returnsNull() {
    Applicant.DwpResponse result =
        applicantMapper.map(
            (uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
                    .DwpResponse)
                null);
    assertNull(result);
  }

  @Test
  void mapApplicantEnum_whenSourceIsValid_mapsCorrectly() {
    uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant.DwpResponse
        source =
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
                .DwpResponse.NO; // "No"

    Applicant.DwpResponse result = applicantMapper.map(source);

    assertNotNull(result);
    assertEquals(Applicant.DwpResponse.NO, result);
    assertEquals(source.value(), result.value()); // both "No"
  }

  // --- mapDwpResponse(MaatApplicationExternal) ---

  @Test
  void mapDwpResponse_whenApplicantDwpResponseIsNull_returnsNull() {
    MaatApplicationExternal external = new MaatApplicationExternal();
    ClientDetails clientDetails = new ClientDetails();
    uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant applicant =
        new uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant();

    applicant.setDwpResponse(null);
    clientDetails.setApplicant(applicant);
    external.setClientDetails(clientDetails);

    Applicant.DwpResponse result = applicantMapper.mapDwpResponse(external);
    assertNull(result);
  }

  @Test
  void mapDwpResponse_whenApplicantDwpResponseIsValid_mapsCorrectly() {
    MaatApplicationExternal external = new MaatApplicationExternal();
    ClientDetails clientDetails = new ClientDetails();
    uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant applicant =
        new uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant();

    applicant.setDwpResponse(
        uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant.DwpResponse
            .YES); // "Yes"

    clientDetails.setApplicant(applicant);
    external.setClientDetails(clientDetails);

    Applicant.DwpResponse result = applicantMapper.mapDwpResponse(external);

    assertNotNull(result);
    assertEquals(Applicant.DwpResponse.YES, result);
    assertEquals("Yes", result.value());
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
  void shouldSuccessfullyMapWhenNoFixedAbode() {
    uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant.ResidenceType
        nfaResidence =
            uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.Applicant
                .ResidenceType.NONE;

    boolean isNoFixedAbode = applicantMapper.mapNoFixedAbode(nfaResidence);

    assertTrue(isNoFixedAbode);
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
        Arguments.of("MaatApplication_partner.json", InvolvementInCase.NONE, "NOCON"));
  }
}
