package uk.gov.justice.laa.crime.applications.adaptor.apispecification;

import static net.serenitybdd.rest.SerenityRest.given;

import io.restassured.response.ValidatableResponse;

public class MaatCourtDataAPI {

  private static final String EFORM_STAGING_URI = "api/eform/{usn}";

  private static final String EFORM_HISTORY_URI = "api/eform/history/{usn}";

  public void deleteEFormStagingByUsn(Integer usn) {
    given()
        .spec(RequestSpecificationBuilder.getMaatAPICrimeApplyReqSpec())
        .pathParam("usn", usn)
        .delete(EFORM_STAGING_URI)
        .then()
        .log()
        .all();
  }

  public ValidatableResponse getEFormStatingByUsn(Integer usn) {
    return given()
        .spec(RequestSpecificationBuilder.getMaatAPICrimeApplyReqSpec())
        .pathParam("usn", usn)
        .get(EFORM_STAGING_URI)
        .then()
        .log()
        .all();
  }

  public void deleteEFormHistoryByUsn(Integer usn) {
    given()
        .spec(RequestSpecificationBuilder.getMaatAPICrimeApplyReqSpec())
        .pathParam("usn", usn)
        .delete(EFORM_HISTORY_URI)
        .then()
        .log()
        .all();
  }

  public ValidatableResponse getEFormHistoryByUsn(Integer usn) {
    return given()
        .spec(RequestSpecificationBuilder.getMaatAPICrimeApplyReqSpec())
        .pathParam("usn", usn)
        .get(EFORM_HISTORY_URI)
        .then()
        .log()
        .all();
  }
}
