package uk.gov.justice.laa.crime.applications.adaptor.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;
@HttpExchange()
public interface MaatCourtDataApiClient {
    @PostExchange("/initialise/{usn}")
    EformStagingResponse retrieveOrInsertDummyUsnRecordInEformStaging(@PathVariable long usn);

}
