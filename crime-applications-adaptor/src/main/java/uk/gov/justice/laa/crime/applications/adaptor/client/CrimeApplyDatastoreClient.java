package uk.gov.justice.laa.crime.applications.adaptor.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import uk.gov.justice.laa.crime.applications.adaptor.model.EformStagingResponse;

import java.util.Map;

@HttpExchange
public interface CrimeApplyDatastoreClient {

    /*
     * TODO Something isn't right, the return type of this should be
     * maat-court-data-api/src/main/java/gov/uk/courtdata/eform/model/EformStagingResponse.java
     * But this return type doesn't contain the XML data field
     * Need to change laa-maat-court-data-api to include XML String in the response from the
     * GET
     */
    @GetExchange("/{usn}")
    EformStagingResponse getApplicationDetails(@PathVariable Long usn,
                                               @RequestHeader Map<String, String> headers);

}
