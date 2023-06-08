package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.Applicant;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.ClientDetails;
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
    void shouldSuccessfullyMapNullCrimeApplyClientDetailsToEmptyAdapterApplicant() throws JSONException {
        ClientDetails nullCrimeApplyClientDetails = null;

        Applicant actualApplicant = applicantMapper.map(nullCrimeApplyClientDetails);

        String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
        JSONAssert.assertEquals("{}", actualApplicantJSON, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapNullCrimeApplyApplicantToEmptyAdapterApplicant() throws JSONException {
        ClientDetails crimeApplyClientDetails = TestData.getMaatApplication().getClientDetails();
        crimeApplyClientDetails.setApplicant(null);

        Applicant actualApplicant = applicantMapper.map(crimeApplyClientDetails);

        String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
        JSONAssert.assertEquals("{}", actualApplicantJSON, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapPopulatedCrimeApplyClientDetailsToAdapterApplicant() throws JSONException {
        ClientDetails crimeApplyClientDetails = TestData.getMaatApplication().getClientDetails();

        Applicant actualApplicant = applicantMapper.map(crimeApplyClientDetails);

        String actualApplicantJSON = JsonUtils.objectToJson(actualApplicant);
        String expectedApplicantJSON = FileUtils.readFileToString("data/expected/crimeapplication/Applicant_mapped.json");
        JSONAssert.assertEquals(expectedApplicantJSON, actualApplicantJSON, JSONCompareMode.STRICT);
    }
}