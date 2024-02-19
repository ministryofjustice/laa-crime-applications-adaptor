package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.InitialMeansAssessment;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.maat.IncomeDetails;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

class InitialMeansAssessmentMapperTest {

    private InitialMeansAssessmentMapper initialMeansAssessmentMapper;

    @BeforeEach
    void setUp() {
        initialMeansAssessmentMapper = new InitialMeansAssessmentMapper();
    }

    @Test
    void shouldSuccessfullyMapCrimeApplyIncomeDetailsToInitialMeansAssessment() throws JSONException {
        IncomeDetails crimeApplyIncomeDetails = TestData.getMaatApplication("MaatApplication_unemployed.json").getMeansDetails().getIncomeDetails();

        InitialMeansAssessment initialMeansAssessment = initialMeansAssessmentMapper.map(crimeApplyIncomeDetails);

        String actualInitialMeansAssessmentJSON = JsonUtils.objectToJson(initialMeansAssessment);
        String expectedInitialMeansAssessmentJSON = FileUtils.readFileToString("data/expected/crimeapplication/InitialMeansAssessment_mapped.json");
        JSONAssert.assertEquals(expectedInitialMeansAssessmentJSON, actualInitialMeansAssessmentJSON, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapNullCrimeApplyIncomeDetailsToEmptyAdapterInitialMeansAssessment() throws JSONException {
        IncomeDetails crimeApplyIncomeDetails = TestData.getMaatApplication("MaatApplication_unemployed.json").getMeansDetails().getIncomeDetails();

        crimeApplyIncomeDetails.setBenefits(null);
        crimeApplyIncomeDetails.setOtherIncome(null);
        crimeApplyIncomeDetails.setDependants(null);

        InitialMeansAssessment initialMeansAssessment = initialMeansAssessmentMapper.map(crimeApplyIncomeDetails);

        String actualInitialMeansAssessmentJSON = JsonUtils.objectToJson(initialMeansAssessment);
        JSONAssert.assertEquals("{}", actualInitialMeansAssessmentJSON, JSONCompareMode.STRICT);
    }

}
