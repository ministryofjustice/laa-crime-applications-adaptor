package uk.gov.justice.laa.crime.applications.adaptor.apispecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.utils.JwtUtil;
import uk.gov.justice.laa.crime.applications.adaptor.apispecification.utils.OAuthTokenUtil;
import uk.gov.justice.laa.crime.applications.adaptor.support.TestConfiguration;

/**
 * This class builds the base request specifications for different API calls used in the test
 * scenarios. Some of these methods relating to the Crime Apply Mock API are candidate for
 * crime-commons-testing-utils module.
 */
public class RequestSpecificationBuilder {

  private static final String CAM_BASE_URL = TestConfiguration.get("cam.base.url");
  private static final String CAM_JWT_SECRET = TestConfiguration.get("cam.jwt.secret");
  private static final String CAM_JWT_ISSUER = TestConfiguration.get("cam.jwt.issuer");
  private static final String CAA_BASE_URL = TestConfiguration.get("caa.base.url");
  private static final String CAA_OAUTH_BASE_URL = TestConfiguration.get("caa.oauth.base.url");
  private static final String CAA_OAUTH_TOKEN_URI = TestConfiguration.get("caa.oauth.token.uri");
  private static final String CAA_OAUTH_CLIENT_ID = TestConfiguration.get("caa.oauth.client.id");
  private static final String CAA_OAUTH_CLIENT_SECRET = TestConfiguration.get(
      "caa.oauth.client.secret");
  private static final String MAAT_CD_BASE_URL = TestConfiguration.get("maat.api.base.url");
  private static final String MAAT_CD_AUTH_BASE_URL = TestConfiguration.get(
      "maat.api.oauth.base.url");
  private static final String MAAT_CD_AUTH_CAA_CLIENT_ID = TestConfiguration.get(
      "maat.api.oauth.client.id");
  private static final String MAAT_CD_AUTH_CAA_CLIENT_SECRET = TestConfiguration.get(
      "maat.api.oauth.client.secret");
  private static final String MAAT_CD_AUTH_TOKEN_URI = TestConfiguration.get(
      "maat.api.oauth.token.uri");

  private RequestSpecificationBuilder() {
  }

  public static RequestSpecification getCAMCrimeApplyReqSpec() {
    return getCAMReqSpec(CAM_BASE_URL, CAM_JWT_ISSUER, CAM_JWT_SECRET);
  }

  public static RequestSpecification getCAMReqSpec(String baseUrl, String jwtIssuer, String jwtSecret) {
    RequestSpecBuilder requestSpecBuilder = setUpRequestSpecBuilder(baseUrl);
    requestSpecBuilder.addHeader("Authorization",
        "Bearer " + JwtUtil.generateJwt(jwtIssuer, jwtSecret));
    return requestSpecBuilder.build();
  }

  public static RequestSpecification getCAACrimeApplyReqSpec() {
    return getCAAReqSpec(CAA_BASE_URL, CAA_OAUTH_BASE_URL, CAA_OAUTH_CLIENT_ID, CAA_OAUTH_CLIENT_SECRET,
            CAA_OAUTH_TOKEN_URI);
  }

  public static RequestSpecification getCAAReqSpec(String baseUrl, String authUrl,
                                                   String authClientId, String authClientSecret, String authTokenUri) {
    RequestSpecBuilder requestSpecBuilder = setUpRequestSpecBuilder(baseUrl);
    requestSpecBuilder.addHeader(
        "Authorization", "Bearer " + OAuthTokenUtil.getAccessToken(
                    authUrl, authClientId, authClientSecret, authTokenUri));
    return requestSpecBuilder.build();
  }

  public static RequestSpecification getMaatAPICrimeApplyReqSpec() {
    return getMaatApiReqSpec(MAAT_CD_BASE_URL, MAAT_CD_AUTH_BASE_URL, MAAT_CD_AUTH_CAA_CLIENT_ID,
        MAAT_CD_AUTH_CAA_CLIENT_SECRET, MAAT_CD_AUTH_TOKEN_URI);
  }

  private static RequestSpecification getMaatApiReqSpec(String baseUrl, String authUrl,
      String authClientId, String authClientSecret, String authTokenUri) {
    RequestSpecBuilder requestSpecBuilder = setUpRequestSpecBuilder(baseUrl);
    requestSpecBuilder.addHeader(
        "Authorization", "Bearer " + OAuthTokenUtil.getAccessToken(
            authUrl, authClientId, authClientSecret, authTokenUri));
    return requestSpecBuilder.build();
  }

  private static RequestSpecBuilder setUpRequestSpecBuilder(String baseUrl) {
    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    requestSpecBuilder.setBaseUri(baseUrl);
    requestSpecBuilder.setContentType(JSON);
    requestSpecBuilder.log(ALL);
    return requestSpecBuilder;
  }
}
