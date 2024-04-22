package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.Applicant;
import uk.gov.justice.laa.crime.model.common.crimeapplicationsadaptor.common.EmploymentStatus;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MeansPassport;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.general.EmploymentType;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

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

  @Test
  void shouldMapEmploymentTypeNotWorkingToEmploymentStatusNonPass() {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication("MaatApplication_unemployed.json");
    List<EmploymentType> employmentType = List.of(EmploymentType.NOT_WORKING);
    maatApplicationExternal.getMeansDetails().getIncomeDetails().setEmploymentType(employmentType);

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    assertEquals(EmploymentStatus.Code.NONPASS, actualApplicant.getEmploymentStatus().getCode());
  }

  @Test
  void shouldMapEmploymentTypeEmployedToEmploymentStatusEmploy() {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication("MaatApplication_unemployed.json");
    List<EmploymentType> employmentType = List.of(EmploymentType.EMPLOYED);
    maatApplicationExternal.getMeansDetails().getIncomeDetails().setEmploymentType(employmentType);

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    assertEquals(EmploymentStatus.Code.EMPLOY, actualApplicant.getEmploymentStatus().getCode());
  }

  @Test
  void shouldMapEmploymentTypeSelfEmployedToEmploymentStatusSelf() {
    MaatApplicationExternal maatApplicationExternal =
        TestData.getMaatApplication("MaatApplication_unemployed.json");
    List<EmploymentType> employmentType = List.of(EmploymentType.SELF_EMPLOYED);
    maatApplicationExternal.getMeansDetails().getIncomeDetails().setEmploymentType(employmentType);

    Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

    assertEquals(EmploymentStatus.Code.SELF, actualApplicant.getEmploymentStatus().getCode());
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
}
