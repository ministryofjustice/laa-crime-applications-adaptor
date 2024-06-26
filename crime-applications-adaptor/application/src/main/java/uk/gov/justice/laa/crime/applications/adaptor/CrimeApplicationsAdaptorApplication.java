package uk.gov.justice.laa.crime.applications.adaptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CrimeApplicationsAdaptorApplication {

  public static void main(String[] args) {
    SpringApplication.run(CrimeApplicationsAdaptorApplication.class, args);
  }
}
