package uk.gov.justice.laa.crime.applications.adaptor.apispecification.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

/**
 * Utility class to create JSON web tokens to access the Crime Apply Datastore and Crime Apply Mock
 * This is a candidate for crime-commons-testing-utils module.
 */
public class JwtUtil {

  private JwtUtil() {}

  public static String generateJwt(String issuer, String clientSecret) {
    return Jwts.builder()
        .issuer(issuer)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 60_000))
        .signWith(Keys.hmacShaKeyFor(clientSecret.getBytes()), SIG.HS256)
        .compact();
  }
}
