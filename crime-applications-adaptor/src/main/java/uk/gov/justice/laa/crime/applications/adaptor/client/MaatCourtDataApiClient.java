package uk.gov.justice.laa.crime.applications.adaptor.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformsHistory;

@HttpExchange()
public interface MaatCourtDataApiClient {
  @PostExchange("/initialise/{usn}")
  EformStagingResponse retrieveOrInsertDummyUsnRecordInEformStaging(
      @PathVariable long usn, @RequestParam String userCreated);

  @PostExchange("/history")
  void createEformsHistoryRecord(@RequestBody EformsHistory eformsHistory);
}
