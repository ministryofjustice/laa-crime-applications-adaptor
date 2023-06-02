package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;

public class TestData {

    private TestData() {
    }

    public static MaatCaaContract getMaatCaaContract(String stubDataFile) {
        String maatCaaContractJson = FileUtils.readFileToString("data/crimeapplicationsadaptor/" + stubDataFile);
        return JsonUtils.jsonToObject(maatCaaContractJson, MaatCaaContract.class);
    }

    public static MaatCaaContract getMaatCaaContract() {
        return getMaatCaaContract("MaatCaaContract_default.json");
    }

    public static MaatApplication getMaatApplication() {
        String maatApplicationJson = FileUtils.readFileToString("data/criminalapplicationsdatastore/MaatApplication_default.json");
        return JsonUtils.jsonToObject(maatApplicationJson, MaatApplication.class);
    }

    public static EformStagingResponse getEformStagingResponse(String stubDataFile) {
        String eformStagingResponseJson = FileUtils.readFileToString("data/eformstaging/" + stubDataFile);
        return JsonUtils.jsonToObject(eformStagingResponseJson, EformStagingResponse.class);
    }
}
