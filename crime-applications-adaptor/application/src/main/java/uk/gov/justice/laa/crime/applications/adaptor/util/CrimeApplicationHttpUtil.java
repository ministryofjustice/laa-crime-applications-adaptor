package uk.gov.justice.laa.crime.applications.adaptor.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.SIG.HS256;

@UtilityClass
public class CrimeApplicationHttpUtil {
    private static final String AUTHORIZATION = "Authorization";
    private static final long TOKEN_LIFETIME_DURATION = Duration.ofSeconds(60).toMillis();

    public Map<String, String> getHttpHeaders(String clientSecret, String issuer) {

        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, generateJWTForCrimeApplyService(clientSecret, issuer));
        return headers;
    }

    private String generateJWTForCrimeApplyService(String clientSecret, String issuer) {
        return "Bearer " + Jwts.builder()
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_LIFETIME_DURATION))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(clientSecret)), HS256)
                .compact();
    }
}
