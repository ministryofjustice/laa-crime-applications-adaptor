package uk.gov.justice.laa.crime.applications.adaptor.stepdefinition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static uk.gov.justice.laa.crime.applications.adaptor.utils.SchemaValidationUtils.assertMatchesSchema;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.io.File;
import java.io.IOException;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import org.htmlunit.jetty.http.HttpStatus;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.CrimeApplicationsAdaptorAPI;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.CrimeApplyMockAPI;

/** This class holds the test steps (e.g. given, when, then) for the test scenarios. */
public class APITestSteps {

  private static final String RESPONSE_KEY = "caaResponse";
  private static final String USN_KEY = "usn";
  private static final String CAA_APPLICATION_SCHEMA =
      "schemas/criminalapplicationsdatastore/maat_application_external.json";
  private static final String CRIME_APPLY_RESOURCE_LOCATION =
      "src/test/resources/testdata/crimeapply/";
  private static final String EXPECTED_RESPONSE_FILE_PATH_BASE =
      "src/test/resources/testdata/expectedresponses/";

  @Steps CrimeApplicationsAdaptorAPI crimeApplicationsAdaptorAPI;

  @Steps CrimeApplyMockAPI crimeApplyMockAPI;

  @Given("an application with usn {int} exists in the datastore")
  public void theFollowingApplicationExistsInTheDatastore(int usn) {
    String crimeApplyJsonPath =
        String.format("%scrimeapplytestdata%s.json", CRIME_APPLY_RESOURCE_LOCATION, usn);
    crimeApplyMockAPI.createNewMockCrimeApplication(usn, crimeApplyJsonPath);
  }

  @When("the GET internal V1 crimeapply endpoint is called with usn {int} and user {string}")
  public void theGetCrimeApplyEndpointIsCalledWith(int usn, String user) {
    ValidatableResponse response = crimeApplicationsAdaptorAPI.getApplicationByUsnFromCam(usn);

    response.log().all();

    Serenity.setSessionVariable(USN_KEY).to(usn);
    Serenity.setSessionVariable(RESPONSE_KEY).to(response);
  }

  @Then("the returned response should match the contents of {string}")
  public void theFollowingDataShouldBeReturned(String expectedResponseFile)
      throws JSONException, IOException {
    JsonPath expectedJson =
        new JsonPath(new File(EXPECTED_RESPONSE_FILE_PATH_BASE + expectedResponseFile));

    ValidatableResponse validatableResponse =
        (ValidatableResponse) Serenity.getCurrentSession().get(RESPONSE_KEY);

    Response response = validatableResponse.assertThat().statusCode(200).extract().response();

    // validate JSON against our schema using NetworkNT which is draft-06 compliant
    assertMatchesSchema(
        "schemas/criminalapplicationsdatastore/maat_application_external.json",
        response.getBody().asString());

    JSONAssert.assertEquals(
        expectedJson.prettify(), response.body().asPrettyString(), JSONCompareMode.LENIENT);
  }

  @Given("an application with usn {int} does not exists in the datastore")
  public void anApplicationWithUsnUsnDoesNotExistsInTheDatastore(int usn) {
    crimeApplyMockAPI.getCrimeApplyMockApplicationByUsn(usn, HttpStatus.NOT_FOUND_404);
  }

  @Then("the returned response should indicate the application is not found")
  public void theReturnedResponseShouldIndicateTheApplicationIsNotFound() {
    ValidatableResponse validatableResponse = getSessionResponseData();

    Response response =
        validatableResponse.assertThat().statusCode(HttpStatus.NOT_FOUND_404).extract().response();

    JsonPath responseJsonPath = response.jsonPath();
    assertThat(responseJsonPath.getString("error"), equalTo("Not Found"));
    assertThat(responseJsonPath.getString("status"), containsString("404"));
  }

  private ValidatableResponse getSessionResponseData() {
    return Serenity.sessionVariableCalled(RESPONSE_KEY);
  }

  private int getSessionUsn() {
    return Serenity.sessionVariableCalled(USN_KEY);
  }
}
