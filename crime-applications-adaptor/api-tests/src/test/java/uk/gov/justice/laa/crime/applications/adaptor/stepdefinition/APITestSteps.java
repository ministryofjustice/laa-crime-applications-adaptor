package uk.gov.justice.laa.crime.applications.adaptor.stepdefinition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.io.File;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.CrimeApplicationsAdaptorAPI;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.CrimeApplyMockAPI;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.MaatCourtDataAPI;

/** This class holds the test steps (e.g. given, when, then) for the test scenarios. */
public class APITestSteps {

  private static final String RESPONSE_KEY = "caaResponse";
  private static final String USN_KEY = "usn";
  private static final String CAA_APPLICATION_SCHEMA =
      "schemas/crimeapplicationsadaptor/maat_application_internal.json";
  private static final String CRIME_APPLY_RESOURCE_LOCATION =
      "src/test/resources/testdata/crimeapply/";
  private static final String EXPECTED_RESPONSE_FILE_PATH_BASE =
      "src/test/resources/testdata/expectedresponses/";

  @Steps CrimeApplicationsAdaptorAPI crimeApplicationsAdaptorAPI;

  @Steps CrimeApplyMockAPI crimeApplyMockAPI;

  @Steps MaatCourtDataAPI maatCourtDataAPI;

  @Given("an application with usn {int} exists in the datastore")
  public void theFollowingApplicationExistsInTheDatastore(int usn) {
    String crimeApplyJsonPath =
        String.format("%scrimeapplytestdata%s.json", CRIME_APPLY_RESOURCE_LOCATION, usn);
    crimeApplyMockAPI.createNewMockCrimeApplication(usn, crimeApplyJsonPath);
  }

  @When("the GET internal V1 crimeapply endpoint is called with usn {int} and user {string}")
  public void theGetCrimeApplyEndpointIsCalledWith(int usn, String user) {
    ValidatableResponse response = crimeApplicationsAdaptorAPI.getApplicationByUsn(usn, user);
    response.log().all();

    Serenity.setSessionVariable(USN_KEY).to(usn);
    Serenity.setSessionVariable(RESPONSE_KEY).to(response);
  }

  @Then("the returned response should match the contents of {string}")
  public void theFollowingDataShouldBeReturned(String expectedResponseFile) throws JSONException {
    JsonPath expectedJson =
        new JsonPath(new File(EXPECTED_RESPONSE_FILE_PATH_BASE + expectedResponseFile));

    ValidatableResponse validatableResponse =
        (ValidatableResponse) Serenity.getCurrentSession().get(RESPONSE_KEY);

    Response response =
        validatableResponse
            .assertThat()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(CAA_APPLICATION_SCHEMA))
            .extract()
            .response();

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
    assertThat(responseJsonPath.getString("title"), equalTo("Not Found"));
    assertThat(responseJsonPath.getString("detail"), containsString("404 Not Found from GET"));
  }

  @And("no entry for usn {int} should be present in the EFORMS_STAGING table")
  public void noEntryForTheApplicationShouldBePresentInTheEformsStagingTable(int usn) {
    ValidatableResponse validatableResponse = maatCourtDataAPI.getEFormStatingByUsn(usn);
    validatableResponse.assertThat().statusCode(HttpStatus.NOT_FOUND_404);

    JsonPath responseJsonPath = validatableResponse.extract().body().jsonPath();

    assertThat(responseJsonPath.getString("code"), equalTo("NOT_FOUND"));
    assertThat(
        responseJsonPath.getString("message"),
        equalTo(String.format("The USN [%d] does not exist in the data store.", usn)));
  }

  @And("no entry for usn {int} should be present in the EFORMS_HISTORY table")
  public void noEntryForTheApplicationShouldBePresentInTheEformsHistoryTable(int usn) {
    ValidatableResponse validatableResponse = maatCourtDataAPI.getEFormHistoryByUsn(usn);
    validatableResponse.assertThat().statusCode(HttpStatus.OK_200).body(equalTo("[]"));
  }

  @After
  public void cleanUpTestData() {
    int usnToClean = getSessionUsn();
    maatCourtDataAPI.deleteEFormStagingByUsn(usnToClean);
    maatCourtDataAPI.deleteEFormHistoryByUsn(usnToClean);
  }

  private ValidatableResponse getSessionResponseData() {
    return Serenity.sessionVariableCalled(RESPONSE_KEY);
  }

  private int getSessionUsn() {
    return Serenity.sessionVariableCalled(USN_KEY);
  }
}
