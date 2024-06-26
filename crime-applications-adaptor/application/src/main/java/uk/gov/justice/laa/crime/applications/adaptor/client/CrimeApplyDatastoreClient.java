package uk.gov.justice.laa.crime.applications.adaptor.client;

import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import uk.gov.justice.laa.crime.model.common.criminalapplicationsdatastore.MaatApplicationExternal;

@HttpExchange
public interface CrimeApplyDatastoreClient {

  @GetExchange("/{usn}")
  MaatApplicationExternal getApplicationDetails(
      @PathVariable long usn, @RequestHeader Map<String, String> headers);
}
