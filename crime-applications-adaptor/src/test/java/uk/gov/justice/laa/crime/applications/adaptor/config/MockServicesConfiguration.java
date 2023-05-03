package uk.gov.justice.laa.crime.applications.adaptor.config;

public class MockServicesConfiguration {

    public static ServicesConfiguration getConfiguration() {
        String host = String.format("http://localhost:%s", 8080);
        ServicesConfiguration servicesConfiguration = new ServicesConfiguration();
        ServicesConfiguration.CrimeApplyApi crimeApplyConfiguration = new ServicesConfiguration.CrimeApplyApi(
                host, "XUBhVjBtRXlpQGNDJGZIKU5MQUVtd2NPY0FbLVN6JGg=", "maat-adapter");
        servicesConfiguration.setCrimeApplyApi(crimeApplyConfiguration);
        return servicesConfiguration;
    }
}
