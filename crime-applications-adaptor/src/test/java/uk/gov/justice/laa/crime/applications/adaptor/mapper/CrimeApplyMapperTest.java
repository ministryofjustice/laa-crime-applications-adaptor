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
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

import static org.junit.jupiter.api.Assertions.assertNull;

class CrimeApplyMapperTest {

    private CrimeApplyMapper crimeApplyMapper;

    @BeforeEach
    void setUp() {
        crimeApplyMapper = new CrimeApplyMapper();
    }

    @Test
    void shouldMapAllRequiredFieldsFromMaatApplicationResponse_to_CrimeApplication() throws JSONException {
        MaatApplication crimeApplyApplicationDetails = TestData.getMaatApplication("MaatApplication_toBeMapped.json");

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(crimeApplyApplicationDetails);

        String actualCrimeApplicationJson = JsonUtils.objectToJson(crimeApplication);
        String expectedCrimeApplicationJson = FileUtils.readFileToString("data/expected/crimeapplication/CrimeApplication_mapped.json");

        JSONAssert.assertEquals(expectedCrimeApplicationJson, actualCrimeApplicationJson, JSONCompareMode.STRICT);
    }

    @Test
    void shouldMapNoHomeAddressFromMaatApplicationResponse_to_CrimeApplication() throws JSONException {
        MaatApplication crimeApplyApplicationDetails = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");


        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(crimeApplyApplicationDetails);

        String actualCrimeApplicationJson = JsonUtils.objectToJson(crimeApplication);
        String expectedCrimeApplicationJson = FileUtils.readFileToString("data/expected/crimeapplication/CrimeApplicationNoHomeAddress_mapped.json");

        JSONAssert.assertEquals(expectedCrimeApplicationJson, actualCrimeApplicationJson, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapWhenNoProviderDetailsAreAvailable() {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.setProviderDetails(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertNull(crimeApplication.getSolicitorName());
        assertNull(crimeApplication.getSolicitorAdminEmail());
        assertNull(crimeApplication.getSupplier());
    }

    @Test
    void shouldSuccessfullyMapWhenNoCaseDetailsAreAvailable() {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.setCaseDetails(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertNull(crimeApplication.getCaseDetails());
        assertNull(crimeApplication.getMagsCourt());
    }

    @Test
    void shouldSuccessfullyMapWhenNoInterestsOfJusticeAreAvailable() {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.setInterestsOfJustice(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertNull(crimeApplication.getInterestsOfJustice());
    }
}