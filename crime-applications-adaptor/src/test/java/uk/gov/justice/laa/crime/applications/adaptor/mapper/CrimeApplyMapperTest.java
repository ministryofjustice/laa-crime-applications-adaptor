package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.CadApplicationResponse;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

class CrimeApplyMapperTest {

    private CrimeApplyMapper crimeApplyMapper;

    @BeforeEach
    void setUp() {
        crimeApplyMapper = new CrimeApplyMapper();
    }

    @Test
    void shouldMapAllRequiredFieldsFromCadApplicationResponse_to_MaatApplication() throws JSONException {
        String crimeApplyApplicationDetailsAsString = FileUtils.readFileToString("data/criminalapplicationsdatastore/CADApplicationResponse_default.json");
        CadApplicationResponse crimeApplyApplicationDetails = JsonUtils.jsonToObject(crimeApplyApplicationDetailsAsString, CadApplicationResponse.class);

        MaatApplication maatApplication = crimeApplyMapper.mapToMaatApplication(crimeApplyApplicationDetails);

        String actualMaatApplicationJson = JsonUtils.objectToJson(maatApplication);
        String expectedMaatApplicationJson = FileUtils.readFileToString("data/expected/maatapplication/MaatApplication_default.json");

        JSONAssert.assertEquals(expectedMaatApplicationJson, actualMaatApplicationJson, JSONCompareMode.STRICT);
    }
}