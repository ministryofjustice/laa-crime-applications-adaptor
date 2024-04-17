package uk.gov.justice.laa.crime.applications.adaptor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.MaatApplicationInternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformStagingService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformsHistoryService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/internal/v1/crimeapply")
@Tag(
    name = "Crime Applications Adaptor",
    description = "Rest API to retrieve application details from crime apply datastore")
public class CrimeApplicationController {

  private static final String DEFAULT_USER = "causer";

  private final CrimeApplicationService crimeApplicationService;
  private final EformStagingService eformStagingService;
  private final EformsHistoryService eformsHistoryService;

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
  @ApiResponse(
      responseCode = "400",
      description = "Bad request.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
              schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Server Error.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
              schema = @Schema(implementation = ProblemDetail.class)))
  public MaatApplicationInternal getCrimeApplyData(
      @PathVariable long id, @PathVariable String userCreated) {
    log.info("Get applicant details from Crime Apply datastore");
    userCreated = StringUtils.isNotEmpty(userCreated) ? userCreated : DEFAULT_USER;
    MaatApplicationInternal maatApplicationInternal =
        crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(id);
    EformStagingResponse eformStagingResponse =
        eformStagingService.retrieveOrInsertDummyUsnRecord(id, userCreated);
    eformsHistoryService.createEformsHistoryRecord(id, userCreated);
    Integer maatRef = eformStagingResponse.getMaatRef();
    if (maatRef != null) {
      return maatApplicationInternal.withMaatRef(maatRef);
    }
    return maatApplicationInternal;
  }
}
