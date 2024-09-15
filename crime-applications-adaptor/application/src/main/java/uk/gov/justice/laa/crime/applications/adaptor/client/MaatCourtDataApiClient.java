package uk.gov.justice.laa.crime.applications.adaptor.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import uk.gov.justice.laa.crime.applications.adaptor.model.RepOrderState;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformStagingResponse;
import uk.gov.justice.laa.crime.applications.adaptor.model.eform.EformsHistory;

@HttpExchange()
public interface MaatCourtDataApiClient {
  @PostExchange("/eform/initialise/{usn}")
  EformStagingResponse retrieveOrInsertDummyUsnRecordInEformStaging(
      @PathVariable long usn, @RequestParam String userCreated);

  @PostExchange("/eform/history")
  void createEformsHistoryRecord(@RequestBody EformsHistory eformsHistory);

  @GetExchange("/internal/v1/assessment/rep-orders/usn/{usn}")
  RepOrderState retrieveCrimeApplicationResultsByUsn(@PathVariable int usn);
}
