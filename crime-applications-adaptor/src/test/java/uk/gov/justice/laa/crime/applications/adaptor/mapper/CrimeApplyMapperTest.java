package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CrimeApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

class CrimeApplyMapperTest {

    private CrimeApplyMapper crimeApplyMapper;

    @BeforeEach
    void setUp() {
        crimeApplyMapper = new CrimeApplyMapper();
    }

    @Test
    void shouldMapAllRequiredFieldsFromMaatApplicationResponse_to_CrimeApplication() throws JSONException {
        String crimeApplyApplicationDetailsAsString = FileUtils.readFileToString("data/criminalapplicationsdatastore/MaatApplication_toBeMapped.json");
        MaatApplication crimeApplyApplicationDetails = JsonUtils.jsonToObject(crimeApplyApplicationDetailsAsString, MaatApplication.class);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(crimeApplyApplicationDetails);

        String actualCrimeApplicationJson = JsonUtils.objectToJson(crimeApplication);
        String expectedCrimeApplicationJson = FileUtils.readFileToString("data/expected/crimeapplication/CrimeApplication_mapped.json");

        JSONAssert.assertEquals(expectedCrimeApplicationJson, actualCrimeApplicationJson, JSONCompareMode.STRICT);
    }

    @Test
    void shouldMapNoHomeAddressFromMaatApplicationResponse_to_CrimeApplication() throws JSONException {
        String crimeApplyApplicationDetailsAsString = FileUtils.readFileToString("data/criminalapplicationsdatastore/MaatApplicationNoHomeAddress_toBeMapped.json");
        MaatApplication crimeApplyApplicationDetails = JsonUtils.jsonToObject(crimeApplyApplicationDetailsAsString, MaatApplication.class);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(crimeApplyApplicationDetails);

        String actualCrimeApplicationJson = JsonUtils.objectToJson(crimeApplication);
        String expectedCrimeApplicationJson = FileUtils.readFileToString("data/expected/CrimeApplication/CrimeApplicationNoHomeAddress_mapped.json");

        JSONAssert.assertEquals(expectedCrimeApplicationJson, actualCrimeApplicationJson, JSONCompareMode.STRICT);
    }
}