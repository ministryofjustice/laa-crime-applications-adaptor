package uk.gov.justice.laa.crime.applications.adaptor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.applications.adaptor.annotation.StandardApiResponse;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationResultService;
import uk.gov.justice.laa.crime.model.common.atis.CrimeApplicationResult;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/external/v1/crime-application/result")
@Tag(
    name = "Crime Applications Adaptor",
    description = "Rest API to return crime application result to crime apply")
public class CrimeApplicationResultController {

  private final CrimeApplicationResultService crimeApplicationResultService;

  @GetMapping(value = "/usn/{usn}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Retrieve application details from crime apply datastore")
  @ApiResponse(
      responseCode = "200",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = CrimeApplicationResult.class)))
  @StandardApiResponse
  public CrimeApplicationResult getCrimeApplicationResultByUsn(@PathVariable int usn) {
    log.info("Invoking Service to get Crime Application Result for usn {}", usn);
    return crimeApplicationResultService.getCrimeApplicationResult(usn);
  }
}
