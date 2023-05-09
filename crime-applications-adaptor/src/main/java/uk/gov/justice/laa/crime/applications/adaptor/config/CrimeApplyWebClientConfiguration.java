package uk.gov.justice.laa.crime.applications.adaptor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;


@Configuration
@Slf4j
public class CrimeApplyWebClientConfiguration {

    @Bean
    WebClient webClient(ServicesConfiguration servicesConfiguration) {
        return WebClient.builder()
                .baseUrl(servicesConfiguration.getCrimeApplyApi().getBaseUrl())
                .build();
    }

    @Bean
    CrimeApplyDatastoreClient postClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
                        .build();
        return httpServiceProxyFactory.createClient(CrimeApplyDatastoreClient.class);
    }
}
