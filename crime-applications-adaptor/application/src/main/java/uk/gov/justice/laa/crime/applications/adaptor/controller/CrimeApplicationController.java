package uk.gov.justice.laa.crime.applications.adaptor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.gov.justice.laa.crime.applications.adaptor.annotation.StandardApiResponse;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.model.common.crimeapplication.MaatApplicationInternal;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/internal/v1/crimeapply")
@Tag(
    name = "Crime Applications Adaptor",
    description = "Rest API to retrieve application details from crime apply datastore")
public class CrimeApplicationController {

  private final CrimeApplicationService crimeApplicationService;
  private final MaatCourtDataApiClient maatCourtDataApiClient;

  @GetMapping(
      value = "/{id}/userCreated/{userCreated}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Retrieve application details from crime apply datastore")
  @ApiResponse(
      responseCode = "200",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = MaatApplicationInternal.class)))
  @StandardApiResponse
  public MaatApplicationInternal getCrimeApplyData(
      @PathVariable long id, @PathVariable String userCreated) {
    log.info("Get applicant details from Crime Apply datastore");
    MaatApplicationInternal maatApplicationInternal =
        crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(id);
    Integer maatRef = maatCourtDataApiClient.retrieveRepOrderIdByUsn(id);
    if (maatRef != null) {
      return maatApplicationInternal.withMaatRef(maatRef);
    }
    return maatApplicationInternal;
  }
}
