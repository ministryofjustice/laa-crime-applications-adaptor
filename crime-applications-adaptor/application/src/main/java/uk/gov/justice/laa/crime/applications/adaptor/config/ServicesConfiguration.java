package uk.gov.justice.laa.crime.applications.adaptor.config;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "services")
public class ServicesConfiguration {

  @NotNull private CrimeApplyApi crimeApplyApi;

  @NotNull private MaatApi maatApi;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CrimeApplyApi {

    @NotNull private String baseUrl;

    @NotNull private String clientSecret;

    @NotNull private String issuer;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MaatApi {

    @NotNull private String baseUrl;

    @NotNull private String registrationId;
  }
}
