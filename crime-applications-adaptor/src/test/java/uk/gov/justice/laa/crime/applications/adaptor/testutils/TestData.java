package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.CrimeApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;

public class TestData {

    private TestData() {
    }

    public static CrimeApplication getCrimeApplication(String stubDataFileName) {
        String crimeApplicationJson = FileUtils.readFileToString("data/crimeapplicationsadaptor/" + stubDataFileName);
        return JsonUtils.jsonToObject(crimeApplicationJson, CrimeApplication.class);
    }

    public static CrimeApplication getCrimeApplication() {
        return getCrimeApplication("CrimeApplication_default.json");
    }

    public static MaatApplication getMaatApplication() {
        return getMaatApplication("MaatApplication_default.json");
    }

    public static EformStagingResponse getEformStagingResponse(String stubDataFileName) {
        String eformStagingResponseJson = FileUtils.readFileToString("data/eformstaging/" + stubDataFileName);
        return JsonUtils.jsonToObject(eformStagingResponseJson, EformStagingResponse.class);
    }

    public static MaatApplication getMaatApplication(String stubDataFileName) {
        String maatApplicationResponseJson = FileUtils.readFileToString("data/criminalapplicationsdatastore/" + stubDataFileName);
        return JsonUtils.jsonToObject(maatApplicationResponseJson, MaatApplication.class);
    }
}
