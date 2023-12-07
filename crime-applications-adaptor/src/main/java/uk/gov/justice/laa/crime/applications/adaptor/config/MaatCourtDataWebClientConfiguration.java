package uk.gov.justice.laa.crime.applications.adaptor.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;

import java.time.Duration;

@Configuration
@AllArgsConstructor
@Slf4j
public class MaatCourtDataWebClientConfiguration {
    @Bean
    WebClient maatCourtDataWebClient(ServicesConfiguration servicesConfiguration, ClientRegistrationRepository clientRegistrations,
                                     OAuth2AuthorizedClientRepository authorizedClients) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrations, authorizedClients
                );
        oauth.setDefaultClientRegistrationId(servicesConfiguration.getEformStagingApi().getRegistrationId());
        ConnectionProvider provider =
                ConnectionProvider.builder("custom")
                        .maxConnections(500)
                        .maxIdleTime(Duration.ofSeconds(20))
                        .maxLifeTime(Duration.ofSeconds(60))
                        .pendingAcquireTimeout(Duration.ofSeconds(60))
                        .evictInBackground(Duration.ofSeconds(120))
                        .build();

        return WebClient.builder()
                .baseUrl(servicesConfiguration.getEformStagingApi().getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(
                                HttpClient.create(provider)
                                        .resolver(DefaultAddressResolverGroup.INSTANCE)
                                        .compress(true)
                                        .responseTimeout(Duration.ofSeconds(30))
                        )
                )
                .filter(oauth)
                .build();
    }

    @Bean
    MaatCourtDataApiClient maatCourtDataApiClient(WebClient maatCourtDataWebClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builderFor(WebClientAdapter.create(maatCourtDataWebClient))
                        .build();
        return httpServiceProxyFactory.createClient(MaatCourtDataApiClient.class);
    }


}
