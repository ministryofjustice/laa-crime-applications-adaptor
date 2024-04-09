package uk.gov.justice.laa.crime.applications.adaptor.stepdefinition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.File;
import net.serenitybdd.annotations.Steps;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.CrimeApplicationsAdaptorAPI;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.CrimeApplyMockAPI;

/**
 * This class holds the test steps (e.g. given, when, then) for the test scenarios.
 */
public class APITestSteps {

  private static final String EXPECTED_RESPONSE_FILE_PATH_BASE = "src/functionalApiTest/resources/testdata/expectedresponses/";
  @Steps
  CrimeApplicationsAdaptorAPI crimeApplicationsAdaptorAPI;

  @Steps
  CrimeApplyMockAPI crimeApplyMockAPI;

  Response caaResponse;

  @Given("an application with usn {int} exists in the datastore")
  public void theFollowingApplicationExistsInTheDatastore(int usn) {
    crimeApplyMockAPI.createNewMockCrimeApplication(usn);
  }

  @When("the GET internal V1 crimeapply endpoint is called with usn {int} and user {string}")
  public void theGetCrimeApplyEndpointIsCalledWith(int usn, String user) {
    caaResponse = crimeApplicationsAdaptorAPI.getApplicationByUsn(usn, user);
  }

  @Then("the returned response should match the contents of {string}")
  public void theFollowingDataShouldBeReturned(String expectedResponseFile) {
    JsonPath expectedJson = new JsonPath(new File(EXPECTED_RESPONSE_FILE_PATH_BASE + expectedResponseFile));
    assertThat(caaResponse.body().jsonPath().getMap(""), equalTo(expectedJson.getMap("")));
  }
}