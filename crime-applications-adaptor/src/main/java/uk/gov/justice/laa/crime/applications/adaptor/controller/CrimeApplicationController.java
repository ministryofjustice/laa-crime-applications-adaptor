package uk.gov.justice.laa.crime.applications.adaptor.controller;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapplicationsadaptor.MaatApplicationInternal;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformStagingService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/internal/v1/crimeapply")
@Tag(name = "Crime Applications Adaptor", description = "Rest API to retrieve application details from crime apply datastore")
public class CrimeApplicationController {

    private CrimeApplicationService crimeApplicationService;
    private EformStagingService eformStagingService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve application details from crime apply datastore")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MaatApplicationInternal.class)
            )
    )
    @ApiResponse(responseCode = "400",
            description = "Bad request.",
            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)
            )
    )
    @Timed(value = "getCrimeApplyData_time_in_nano_seconds", description = "Time taken to complete the execution of getCrimeApplyData in nano-seconds")
    @Counted(value = "getCrimeApplyData_count", recordFailuresOnly = true,
            extraTags = {"sample_label", "sample_label_value"}, description = "The number of getCrimeApplyData failures")
    public MaatApplicationInternal getCrimeApplyData(@PathVariable long id) {
        log.info("Get applicant details from Crime Apply datastore");
        MaatApplicationInternal maatApplicationInternal = crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(id);
        EformStagingResponse eformStagingResponse = eformStagingService.retrieveOrInsertDummyUsnRecord(id);
        Integer maatRef = eformStagingResponse.getMaatRef();
        if (maatRef != null) {
            return maatApplicationInternal
                    .withMaatRef(maatRef);
        }
        return maatApplicationInternal;
    }
}
