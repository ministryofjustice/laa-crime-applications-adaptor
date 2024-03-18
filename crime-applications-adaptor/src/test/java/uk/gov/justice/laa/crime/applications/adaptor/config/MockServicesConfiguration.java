package uk.gov.justice.laa.crime.applications.adaptor.config;

public class MockServicesConfiguration {

  private static final String CLIENT_SECRET = "TESTVjBtRXlpQGNDJGZIKU5MQUVtd2NPY0FbLVN6JGg=";
  private static final String ISSUER = "maat-adapter";
  private static final int PORT_NO = 8080;

  public static ServicesConfiguration getConfiguration() {
    String host = String.format("http://localhost:%s", PORT_NO);
    ServicesConfiguration servicesConfiguration = new ServicesConfiguration();
    ServicesConfiguration.CrimeApplyApi crimeApplyConfiguration =
        new ServicesConfiguration.CrimeApplyApi(host, CLIENT_SECRET, ISSUER);
    servicesConfiguration.setCrimeApplyApi(crimeApplyConfiguration);
    return servicesConfiguration;
  }
}
