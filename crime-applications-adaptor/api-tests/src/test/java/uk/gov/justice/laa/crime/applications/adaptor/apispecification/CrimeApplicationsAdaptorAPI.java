package uk.gov.justice.laa.crime.applications.adaptor.apispecification;

import static io.restassured.RestAssured.given;

import io.restassured.response.ValidatableResponse;

/**
 * This class holds the API specifications for the Crime Applications Adaptor endpoints under test.
 */
public class CrimeApplicationsAdaptorAPI {

  private static final String CAA_GET_URI = "api/internal/v1/crimeapply/{usn}/userCreated/{user}";

  public ValidatableResponse getApplicationByUsn(int usn, String user) {
    return given()
        .spec(RequestSpecificationBuilder.getCAACrimeApplyReqSpec())
        .pathParam("usn", usn)
        .pathParam("user", user)
        .get(CAA_GET_URI)
        .then();
  }
}
