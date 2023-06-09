package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.hamcrest.collection.IsEmptyCollection;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CrimeApplyMapperTest {

    private CrimeApplyMapper crimeApplyMapper;

    @BeforeEach
    void setUp() {
        crimeApplyMapper = new CrimeApplyMapper();
    }

    @Test
    void shouldMapAllRequiredFieldsFromMaatApplicationResponse_to_CrimeApplication() throws JSONException {
        MaatApplication crimeApplyApplicationDetails = TestData.getMaatApplication("MaatApplication_6000308.json");

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(crimeApplyApplicationDetails);

        String actualCrimeApplicationJson = JsonUtils.objectToJson(crimeApplication);
        String expectedCrimeApplicationJson = JsonUtils.objectToJson(TestData.getCrimeApplication("CrimeApplication_6000308.json"));

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
    void shouldSuccessfullyMapWhenNoProviderDetailsAreAvailable() throws JSONException {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.setProviderDetails(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertNull(crimeApplication.getSolicitorName());
        assertNull(crimeApplication.getSolicitorAdminEmail());
        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(crimeApplication.getSupplier()), JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapWhenNoCaseDetailsAreAvailable() throws JSONException {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.setCaseDetails(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(crimeApplication.getCaseDetails()), JSONCompareMode.STRICT);
        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(crimeApplication.getMagsCourt()), JSONCompareMode.STRICT);
        assertNull(crimeApplication.getHearingDate());
    }

    @Test
    void shouldSuccessfullyMapWhenNoInterestsOfJusticeAreAvailable() throws JSONException {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.setInterestsOfJustice(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertThat(crimeApplication.getInterestsOfJustice(), IsEmptyCollection.empty());
    }

    @Test
    void shouldSuccessfullyMapWhenCaseDetailsHaveNullCaseType() {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.getCaseDetails().setCaseType(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertNull(crimeApplication.getCaseDetails().getCaseType());
    }

    @Test
    void shouldSuccessfullyMapWhenCaseDetailsHaveNullOffenceClass() {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.getCaseDetails().setOffenceClass(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertNull(crimeApplication.getCaseDetails().getOffenceClass());
    }

    @Test
    void shouldSuccessfullyMapWhenApplicantCorrespondenceAddressTypeIsNull() {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.getClientDetails().getApplicant().setCorrespondenceAddressType(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertFalse(crimeApplication.getApplicant().getUseSupplierAddressForPost());
    }

    @Test
    void shouldSuccessfullyMapWhenClientDetailsAreNull() throws JSONException {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.setClientDetails(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(crimeApplication.getApplicant()), JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapWhenApplicantIsNull() throws JSONException {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.getClientDetails().setApplicant(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(crimeApplication.getApplicant()), JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapWhenThereIsInterestsOfJusticeWithNullType() {
        MaatApplication maatApplication = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplication.getInterestsOfJustice().get(0).setType(null);

        CrimeApplication crimeApplication = crimeApplyMapper.mapToCrimeApplication(maatApplication);

        assertNull(crimeApplication.getInterestsOfJustice().get(0).getType());
        assertEquals("More details about loss of liberty.", crimeApplication.getInterestsOfJustice().get(0).getReason());
    }
}