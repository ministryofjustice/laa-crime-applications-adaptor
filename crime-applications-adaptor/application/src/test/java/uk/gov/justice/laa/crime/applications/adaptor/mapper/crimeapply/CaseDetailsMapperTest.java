package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.common.CaseDetails;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

class CaseDetailsMapperTest {

    private CaseDetailsMapper caseDetailsMapper;

    @BeforeEach
    void setUp() {
        caseDetailsMapper = new CaseDetailsMapper();
    }

    @Test
    void shouldSuccessfullyMapCrimeApplyCaseDetailsToAdapterCaseDetails() throws JSONException {
        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails crimeApplyCaseDetails
                = TestData.getMaatApplication().getCaseDetails();

        CaseDetails actualCaseDetails = caseDetailsMapper.map(crimeApplyCaseDetails);

        String actualCaseDetailsJSON = JsonUtils.objectToJson(actualCaseDetails);
        JSONAssert.assertEquals("{\"caseType\":\"APPEAL CC\",\"offenceClass\":\"MURDER\",\"appealReceivedDate\":\"2021-10-25\"}", actualCaseDetailsJSON, JSONCompareMode.STRICT);
    }

    @Test
    void shouldMapNullCrimeApplyCaseDetailsToEmptyAdapterCaseDetails() throws JSONException {
        uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CaseDetails nullCrimeApplyCaseDetails
                = null;

        CaseDetails actualCaseDetails = caseDetailsMapper.map(nullCrimeApplyCaseDetails);

        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(actualCaseDetails), JSONCompareMode.STRICT);
    }
}