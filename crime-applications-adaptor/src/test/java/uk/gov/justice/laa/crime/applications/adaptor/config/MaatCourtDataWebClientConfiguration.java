package uk.gov.justice.laa.crime.applications.adaptor.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;

@TestConfiguration
public class MaatCourtDataWebClientConfiguration {
    @Bean
    WebClient maatCourtDataWebClient(ServicesConfiguration servicesConfiguration) {
        return WebClient.builder()
                .baseUrl(servicesConfiguration.getEformStagingApi().getBaseUrl())
                .build();
    }

    @Bean
    MaatCourtDataApiClient maatCourtDataApiClient(WebClient maatCourtDataWebClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(maatCourtDataWebClient))
                        .build();
        return httpServiceProxyFactory.createClient(MaatCourtDataApiClient.class);
    }
}
