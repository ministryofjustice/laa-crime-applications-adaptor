package uk.gov.justice.laa.crime.applications.adaptor.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import uk.gov.justice.laa.crime.applications.adaptor.client.CrimeApplyDatastoreClient;
import uk.gov.justice.laa.crime.applications.adaptor.exception.APIClientException;
import uk.gov.justice.laa.crime.applications.adaptor.exception.RetryableWebClientResponseException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@AllArgsConstructor
@Slf4j
public class CrimeApplyWebClientConfiguration {
    @Bean
    WebClient webClient(ServicesConfiguration servicesConfiguration) {
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
                .clientConnector(new ReactorClientHttpConnector(
                             HttpClient.create(provider)
                                    .resolver(DefaultAddressResolverGroup.INSTANCE)
                                    .compress(true)
                                    .responseTimeout(Duration.ofSeconds(30))
                        )
                )
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        response -> getError(response, "5xxServerError"))
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        response -> getError(response, "4xxClientError"))
                .filter(loggingRequest())
                .filter(loggingResponse())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    CrimeApplyDatastoreClient crimeApplyDatastoreClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient))
                         .blockTimeout(Duration.of(20, ChronoUnit.SECONDS))
                        .build();
        return httpServiceProxyFactory.createClient(CrimeApplyDatastoreClient.class);
    }

    public ExchangeFilterFunction loggingRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    public ExchangeFilterFunction loggingResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    private Mono<Throwable> getError(ClientResponse response, String errorType) {
        if(errorType.equals("5xxServerError")){
            return Mono.error(new RetryableWebClientResponseException(StringUtil.EMPTY_STRING+response.statusCode().value()));
        }
        return Mono.error(new APIClientException(StringUtil.EMPTY_STRING+response.statusCode().value()));
    }

}
