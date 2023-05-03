package uk.gov.justice.laa.crime.applications.adaptor.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
@Slf4j
public class CrimeApplicationUtil {
    public static final String AUTHORIZATION = "Authorization";
    public static final int TIME_IN_MILLISEC = 60000;

    public Map<String, String> getHttpHeaders(String clientSecret, String issuer) {
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, "Bearer " + generateJWTForCrimeApplyService(clientSecret, issuer));
        return headers;
    }

    private String generateJWTForCrimeApplyService(String clientSecret, String issuer) {
        log.info("issuer: "+issuer);
        log.info("client secret: "+clientSecret);
        return Jwts.builder()
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TIME_IN_MILLISEC))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(clientSecret))
                        , SignatureAlgorithm.HS256
                )
                .compact();
    }
}
