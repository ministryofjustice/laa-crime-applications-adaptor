package uk.gov.justice.laa.crime.applications.adaptor.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;

@Configuration
@AllArgsConstructor
@Slf4j
public class CrimeApplyWebClientConfiguration {
  @Bean
  WebClient crimeApplyWebClient(ServicesConfiguration servicesConfiguration) {
    ConnectionProvider provider =
        ConnectionProvider.builder("custom")
            .maxConnections(500)
            .maxIdleTime(Duration.ofSeconds(20))
            .maxLifeTime(Duration.ofSeconds(60))
            .pendingAcquireTimeout(Duration.ofSeconds(60))
            .evictInBackground(Duration.ofSeconds(120))
            .build();

    return WebClient.builder()
        .baseUrl(servicesConfiguration.getCrimeApplyApi().getBaseUrl())
        .clientConnector(
            new ReactorClientHttpConnector(
                HttpClient.create(provider)
                    .resolver(DefaultAddressResolverGroup.INSTANCE)
                    .compress(true)
                    .responseTimeout(Duration.ofSeconds(30))))
        .build();
  }

  @Bean
  CrimeApplyDatastoreClient crimeApplyDatastoreClient(WebClient crimeApplyWebClient) {
    HttpServiceProxyFactory httpServiceProxyFactory =
        HttpServiceProxyFactory.builderFor(WebClientAdapter.create(crimeApplyWebClient)).build();
    return httpServiceProxyFactory.createClient(CrimeApplyDatastoreClient.class);
  }
}
