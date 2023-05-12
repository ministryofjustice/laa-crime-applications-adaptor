package uk.gov.justice.laa.crime.applications.adaptor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import uk.gov.justice.laa.crime.applications.adaptor.client.EformStagingApiClient;

@Configuration
public class EformStagingWebClientConfiguration {
    @Bean
    WebClient eformStagingWebClient(ServicesConfiguration servicesConfiguration) {
        return WebClient.builder()
                .baseUrl(servicesConfiguration.getEformStagingApi().getBaseUrl())
                .build();
    }

    @Bean
    EformStagingApiClient eformStagingApiClient(WebClient eformStagingWebClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(eformStagingWebClient))
                        .build();
        return httpServiceProxyFactory.createClient(EformStagingApiClient.class);
    }
}
