package uk.gov.justice.laa.crime.applications.adaptor.apispecification;

import static io.restassured.RestAssured.given;

import java.io.File;
import org.eclipse.jetty.http.HttpStatus;

/**
 * This class holds the API specification for the Crime Apply Mock API which is used to set up data
 * for the test scenarios This is a candidate for crime-commons-testing-utils module.
 */
public class CrimeApplyMockAPI {

  private static final String CAM_URI = "api/v1/maat/applications/{usn}";

  public void createNewMockCrimeApplication(int usn, String jsonBodyFilePath) {
    given()
        .spec(RequestSpecificationBuilder.getCAMReqSpec())
        .body(new File(jsonBodyFilePath))
        .pathParam("usn", usn)
        .put(CAM_URI)
        .then()
        .log()
        .all()
        .assertThat()
        .statusCode(HttpStatus.OK_200)
        .extract()
        .response();
  }

  public void getCrimeApplyMockApplicationByUsn(int usn, int statusCode) {
    given()
        .spec(RequestSpecificationBuilder.getCAMReqSpec())
        .pathParam("usn", usn)
        .get(CAM_URI)
        .then()
        .log()
        .all()
        .assertThat()
        .statusCode(statusCode)
        .extract()
        .response();
  }
}
