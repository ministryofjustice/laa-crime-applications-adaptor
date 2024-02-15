package uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply;

import org.hamcrest.collection.IsEmptyCollection;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.MaatApplicationInternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplicationExternal;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.TestData;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CrimeApplyMapperTest {

    private CrimeApplyMapper crimeApplyMapper;

    @BeforeEach
    void setUp() {
        crimeApplyMapper = new CrimeApplyMapper();
    }

    @Test
    void shouldMapAllRequiredFieldsFromMaatApplicationResponse_to_CrimeApplication() throws JSONException {
        MaatApplicationExternal crimeApplyApplicationDetails = TestData.getMaatApplication("MaatApplication_6000308.json");

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(crimeApplyApplicationDetails);

        String actualCrimeApplicationJson = JsonUtils.objectToJson(maatApplicationInternal);
        String expectedCrimeApplicationJson = JsonUtils.objectToJson(TestData.getCrimeApplication("CrimeApplication_6000308.json"));

        JSONAssert.assertEquals(expectedCrimeApplicationJson, actualCrimeApplicationJson, JSONCompareMode.STRICT);
    }

    @Test
    void shouldMapNoHomeAddressFromMaatApplicationResponse_to_CrimeApplication() throws JSONException {
        MaatApplicationExternal crimeApplyApplicationDetails = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(crimeApplyApplicationDetails);

        String actualCrimeApplicationJson = JsonUtils.objectToJson(maatApplicationInternal);
        String expectedCrimeApplicationJson = FileUtils.readFileToString("data/expected/crimeapplication/CrimeApplicationNoHomeAddress_mapped.json");
        JSONAssert.assertEquals(expectedCrimeApplicationJson, actualCrimeApplicationJson, JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapWhenNoProviderDetailsAreAvailable() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplicationExternal.setProviderDetails(null);

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(maatApplicationExternal);

        assertNull(maatApplicationInternal.getSolicitorName());
        assertNull(maatApplicationInternal.getSolicitorAdminEmail());
        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(maatApplicationInternal.getSupplier()), JSONCompareMode.STRICT);
    }

    @Test
    void shouldSuccessfullyMapWhenNoCaseDetailsAreAvailable() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplicationExternal.setCaseDetails(null);

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(maatApplicationExternal);

        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(maatApplicationInternal.getCaseDetails()), JSONCompareMode.STRICT);
        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(maatApplicationInternal.getMagsCourt()), JSONCompareMode.STRICT);
        assertNull(maatApplicationInternal.getHearingDate());
    }

    @Test
    void shouldSuccessfullyMapWhenNoInterestsOfJusticeAreAvailable() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(maatApplicationExternal);

        assertThat(maatApplicationInternal.getInterestsOfJustice(), IsEmptyCollection.empty());
    }

    @Test
    void shouldSuccessfullyMapWhenCaseDetailsHaveNullCaseType() {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplicationExternal.getCaseDetails().setCaseType(null);

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(maatApplicationExternal);

        assertNull(maatApplicationInternal.getCaseDetails().getCaseType());
    }

    @Test
    void shouldSuccessfullyMapWhenCaseDetailsHaveNullOffenceClass() {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplicationExternal.getCaseDetails().setOffenceClass(null);

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(maatApplicationExternal);

        assertNull(maatApplicationInternal.getCaseDetails().getOffenceClass());
    }

    @Test
    void shouldSuccessfullyMapWhenApplicantCorrespondenceAddressTypeIsNull() {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplicationExternal.getClientDetails().getApplicant().setCorrespondenceAddressType(null);

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(maatApplicationExternal);

        assertFalse(maatApplicationInternal.getApplicant().getUseSupplierAddressForPost());
    }

    @Test
    void shouldSuccessfullyMapWhenApplicantIsNull() throws JSONException {
        MaatApplicationExternal maatApplicationExternal = TestData.getMaatApplication("MaatApplicationNoHomeAddress_toBeMapped.json");
        maatApplicationExternal.getClientDetails().setApplicant(null);

        MaatApplicationInternal maatApplicationInternal = crimeApplyMapper.mapToCrimeApplication(maatApplicationExternal);

        JSONAssert.assertEquals("{}", JsonUtils.objectToJson(maatApplicationInternal.getApplicant()), JSONCompareMode.STRICT);
    }
}