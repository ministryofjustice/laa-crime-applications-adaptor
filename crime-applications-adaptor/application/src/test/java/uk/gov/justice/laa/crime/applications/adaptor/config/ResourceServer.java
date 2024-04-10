package uk.gov.justice.laa.crime.applications.adaptor.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@TestConfiguration
@EnableWebSecurity
public class ResourceServer {
    public void configure(HttpSecurity http) throws Exception {
        http.build();
    }
}