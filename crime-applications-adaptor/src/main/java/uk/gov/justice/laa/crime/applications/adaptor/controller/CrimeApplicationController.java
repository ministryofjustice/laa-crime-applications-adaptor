package uk.gov.justice.laa.crime.applications.adaptor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.applications.adaptor.model.maat.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.service.CrimeApplicationService;
import uk.gov.justice.laa.crime.applications.adaptor.service.EformStagingService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/internal/v1/crimeapply")
@Tag(name = "Crime Application Adaptor", description = "Rest API to retrieve application details from crime apply datastore")
public class CrimeApplicationController {

    private final CrimeApplicationService crimeApplicationService;
    private final EformStagingService eformStagingService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve application details from crime apply datastore")
    @StandardApiResponseCodes
    public MaatApplication getCrimeApplyData(@PathVariable Long id) {
        log.info("Get applicant details from Crime Apply datastore");
        eformStagingService.retrieveOrInsertDummyUsnRecord(id);
        return crimeApplicationService.retrieveApplicationDetailsFromCrimeApplyDatastore(id);
    }
}
