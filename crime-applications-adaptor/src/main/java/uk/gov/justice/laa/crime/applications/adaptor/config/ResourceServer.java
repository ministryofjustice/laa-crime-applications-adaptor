package uk.gov.justice.laa.crime.applications.adaptor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ResourceServer {

    //Need the API Gateway - Cognito setup in cloud platform.
    //Blocked by https://dsdmoj.atlassian.net/browse/LASB-1828 (WIP).
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.build();
    }
}