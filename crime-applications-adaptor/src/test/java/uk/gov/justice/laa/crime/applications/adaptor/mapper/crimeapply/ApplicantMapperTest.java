package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.EmploymentStatus;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MeansPassport;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.general.EmploymentType;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

class ApplicantMapperTest {

    private ApplicantMapper applicantMapper;

    @BeforeEach
    void setUp() {
        applicantMapper = new ApplicantMapper();
    }

    @Test
    void shouldSuccessfullyMapNullCrimeApplyClientDetailsToEmptyAdapterApplicant() throws JSONException {
        MaatApplicationExternal nullMaatApplicationExternal = null;

        Applicant actualApplicant = applicantMapper.map(nullMaatApplicationExternal);

        String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
        JSONAssert.assertEquals("{}", actualApplicantJSON, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapNullCrimeApplyApplicantToEmptyAdapterApplicant() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplication_unemployed.json");

        maatApplicationExternal.getClientDetails().setApplicant(null);

        Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

        String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
        JSONAssert.assertEquals("{}", actualApplicantJSON, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapPopulatedCrimeApplyClientDetailsToAdapterApplicant() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplication_unemployed.json");

        Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

        String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
        String expectedApplicantJSON = FileUtils.readFileToString("data/expected/crimeapplication/Applicant_mapped.json");
        JSONAssert.assertEquals(expectedApplicantJSON, actualApplicantJSON, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapEmploymentStatusIsEmployedToCrimeApplyClientDetailsToAdapterApplicant() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplication_unemployed.json");
        List<EmploymentType> employmentType = List.of(EmploymentType.EMPLOYED);
        maatApplicationExternal.getMeansDetails().getIncomeDetails().setEmploymentType(employmentType);

        Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

        assertEquals(actualApplicant.getEmploymentStatus().getCode(), EmploymentStatus.Code.EMPLOY);

    }

    @Test
    void shouldSuccessfullyMapEmploymentStatusIsSelfEmployedToCrimeApplyClientDetailsToAdapterApplicant() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplication_unemployed.json");
        List<EmploymentType> employmentType = List.of(EmploymentType.SELF_EMPLOYED);
        maatApplicationExternal.getMeansDetails().getIncomeDetails().setEmploymentType(employmentType);

        Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

        assertEquals(actualApplicant.getEmploymentStatus().getCode(), EmploymentStatus.Code.SELF);

    }

    @Test
    void shouldSuccessfullyMapEmploymentStatusIsPassportedToCrimeApplyClientDetailsToAdapterApplicant() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplication_unemployed.json");
        List<MeansPassport> meansPassport = List.of(MeansPassport.ON_BENEFIT_CHECK);
        maatApplicationExternal.setMeansPassport(meansPassport);

        Applicant actualApplicant = applicantMapper.map(maatApplicationExternal);

        assertEquals(actualApplicant.getEmploymentStatus().getCode(), EmploymentStatus.Code.PASSPORTED);

    }
}