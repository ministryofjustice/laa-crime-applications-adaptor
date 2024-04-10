package uk.gov.justice.laa.crime.applications.adaptor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI baseOpenAPI(){
        return new OpenAPI().info(new Info().title("OpenAPI Definition").version("1.0.0").description("Spring Doc"));
    }
}
