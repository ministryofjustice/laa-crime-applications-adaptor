package uk.gov.justice.laa.crime.applications.adaptor.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import uk.gov.justice.laa.crime.applications.adaptor.model.crimeapply.MaatCaaContract;

import java.util.Map;

@HttpExchange
public interface CrimeApplyDatastoreClient {

    @GetExchange("/{usn}")
    MaatCaaContract getApplicationDetails(@PathVariable Long usn,
                                          @RequestHeader Map<String, String> headers);
}
