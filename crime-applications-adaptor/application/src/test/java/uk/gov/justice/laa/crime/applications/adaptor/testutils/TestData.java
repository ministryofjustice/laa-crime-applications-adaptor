package uk.gov.justice.laa.crime.applications.adaptor.testutils;

import uk.gov.justice.laa.crime.model.common.crimeapplication.MaatApplicationInternal;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;

public class TestData {

  private TestData() {}

  public static MaatApplicationInternal getCrimeApplication(String stubDataFileName) {
    String crimeApplicationJson =
        FileUtils.readFileToString("data/crimeapplicationsadaptor/" + stubDataFileName);
    return JsonUtils.jsonToObject(crimeApplicationJson, MaatApplicationInternal.class);
  }

  public static Integer getMaatRef(String stubDataFileName) {
    String maatRef =
        FileUtils.readFileToString("data/crimeapplicationsadaptor/" + stubDataFileName).strip();
    return Integer.valueOf(maatRef);
  }

  public static MaatApplicationInternal getCrimeApplication() {
    return getCrimeApplication("CrimeApplication_default.json");
  }

  public static MaatApplicationExternal getMaatApplication() {
    return getMaatApplication("MaatApplication_default.json");
  }

  public static MaatApplicationExternal getMaatApplicationWithCapitalDetails() {
    return getMaatApplication("MaatApplication_10000331.json");
  }

  public static MaatApplicationExternal getMaatApplication(String stubDataFileName) {
    String maatApplicationResponseJson =
        FileUtils.readFileToString("data/criminalapplicationsdatastore/" + stubDataFileName);
    return JsonUtils.jsonToObject(maatApplicationResponseJson, MaatApplicationExternal.class);
  }
}
