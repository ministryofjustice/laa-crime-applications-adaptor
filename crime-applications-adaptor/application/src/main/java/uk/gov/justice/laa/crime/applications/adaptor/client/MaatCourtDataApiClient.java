package uk.gov.justice.laa.crime.applications.adaptor.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import uk.gov.justice.laa.crime.applications.adaptor.model.RepOrderState;

@HttpExchange()
public interface MaatCourtDataApiClient {
  @GetExchange("/internal/v1/assessment/rep-orders/usn/{usn}")
  RepOrderState retrieveCrimeApplicationResultsByUsn(@PathVariable int usn);

  @GetExchange("/internal/v1/assessment/rep-orders/rep-order-state/{repId}")
  RepOrderState retrieveCrimeApplicationResultsByRepId(@PathVariable int repId);

  @GetExchange("/internal/v1/assessment/rep-orders")
  Integer retrieveRepOrderIdByUsn(@RequestParam("usn") long usn);
}
