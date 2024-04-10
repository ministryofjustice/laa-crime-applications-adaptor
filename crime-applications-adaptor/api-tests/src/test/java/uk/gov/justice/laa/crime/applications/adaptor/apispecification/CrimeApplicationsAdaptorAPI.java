package uk.gov.justice.laa.crime.applications.adaptor.apispecification;

import static io.restassured.RestAssured.given;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

/**
 * This class holds the API specifications for the Crime Applications Adaptor endpoints under test.
 */
public class CrimeApplicationsAdaptorAPI {

  private static final String CAA_GET_URI = "api/internal/v1/crimeapply/{usn}/userCreated/{user}";

  public Response getApplicationByUsn(int usn, String user, String responseSchemaPath) {
    return given().spec(RequestSpecificationBuilder.getCAAReqSpec())
        .pathParam("usn", usn)
        .pathParam("user", user)
        .get(CAA_GET_URI)
        .then()
        .log().all()
        .assertThat()
        .statusCode(200)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(responseSchemaPath))
        .extract()
        .response();
  }
}
