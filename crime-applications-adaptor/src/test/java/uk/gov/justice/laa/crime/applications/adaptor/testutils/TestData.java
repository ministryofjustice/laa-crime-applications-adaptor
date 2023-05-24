package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import uk.gov.justice.laa.crime.applications.adaptor.model.MaatCaaContract;
import uk.gov.justice.laa.crime.applications.adaptor.model.criminalapplicationsdatastore.MaatApplication;

public class TestData {

    private TestData() {
    }

    public static MaatCaaContract getMaatCaaContract() {
        String maatCaaContractJson = FileUtils.readFileToString("data/crimeapplicationsadaptor/MaatCaaContract_default.json");
        return JsonUtils.jsonToObject(maatCaaContractJson, MaatCaaContract.class);
    }

    public static MaatApplication getMaatApplication() {
        String maatApplicationJson = FileUtils.readFileToString("data/criminalapplicationsdatastore/MaatApplication_default.json");
        return JsonUtils.jsonToObject(maatApplicationJson, MaatApplication.class);
    }
}
