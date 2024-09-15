package uk.gov.justice.laa.crime.applications.adaptor.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "400",
          description = "Bad Request.",
          content =
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "500",
          description = "Server Error.",
          content =
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ProblemDetail.class)))
    })
public @interface StandardApiResponse {}
