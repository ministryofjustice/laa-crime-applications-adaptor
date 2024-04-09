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
 * scenarios.
 * Some of these methods relating to the Crime Apply Mock API are candidate for
 * crime-commons-testing-utils module.
 */
public class RequestSpecificationBuilder {

    private RequestSpecificationBuilder() {
    }

    private static final String CAM_BASE_URL = TestConfiguration.get("cam.base.url");
    private static final String CAM_JWT_SECRET = TestConfiguration.get("cam.jwt.secret");
    private static final String CAM_JWT_ISSUER = TestConfiguration.get("cam.jwt.issuer");
    private static final String CAA_BASE_URL = TestConfiguration.get("caa.base.url");
    private static final String CAA_OAUTH_BASE_URL = TestConfiguration.get("caa.oauth.base.url");
    private static final String CAA_OAUTH_TOKEN_URI = TestConfiguration.get("caa.oauth.token.uri");
    private static final String CAA_OAUTH_CLIENT_ID = TestConfiguration.get("caa.oauth.client.id");
    private static final String CAA_OAUTH_CLIENT_SECRET = TestConfiguration.get("caa.oauth.client.secret");


    public static RequestSpecification getCAMReqSpec() {
        RequestSpecBuilder requestSpecBuilder = setUpRequestSpecBuilder(CAM_BASE_URL);
        requestSpecBuilder.addHeader("Authorization", "Bearer " + JwtUtil.generateJwt(CAM_JWT_ISSUER, CAM_JWT_SECRET));
        return requestSpecBuilder.build();
    }

    public static RequestSpecification getCAAReqSpec() {
        RequestSpecBuilder requestSpecBuilder = setUpRequestSpecBuilder(CAA_BASE_URL);
        requestSpecBuilder.addHeader(
                "Authorization", "Bearer " + OAuthTokenUtil.getAccessToken(
                CAA_OAUTH_BASE_URL, CAA_OAUTH_CLIENT_ID, CAA_OAUTH_CLIENT_SECRET, CAA_OAUTH_TOKEN_URI));
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