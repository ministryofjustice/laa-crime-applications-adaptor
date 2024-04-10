package uk.gov.justice.laa.crime.applications.adaptor.apispecification;

import static io.restassured.RestAssured.given;

import java.io.File;

/**
 * This class holds the API specification for the Crime Apply Mock API which is used to set up
 * data for the test scenarios
 * This is a candidate for crime-commons-testing-utils module.
 */
public class CrimeApplyMockAPI {

    private static final String CRIME_APPLY_RESOURCE_LOCATION = "src/functionalApiTest/resources/testdata/crimeapply/";
    private static final String CAM_PUT_URI = "api/v1/maat/applications/{usn}";

    public void createNewMockCrimeApplication(int usn) {
        String crimeApplyJsonPath = String.format("%scrimeapplytestdata%s.json", CRIME_APPLY_RESOURCE_LOCATION, usn);
        given().spec(RequestSpecificationBuilder.getCAMReqSpec())
            .body(new File(crimeApplyJsonPath))
            .pathParam("usn", usn)
            .put(CAM_PUT_URI)
            .then()
            .log().all()
            .assertThat()
            .statusCode(200)
            .extract()
            .response();
    }
}
