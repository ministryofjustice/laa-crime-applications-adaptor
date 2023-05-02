package uk.gov.justice.laa.crime.applications.adaptor.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class CrimeApplicationUtil {
    public static final String AUTHORIZATION = "Authorization";

    public Map<String, String> getHttpHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, "Bearer " + generateJWTForCrimeApplyService());
        return headers;
    }

    private String generateJWTForCrimeApplyService() {
        return Jwts.builder()
                .setIssuer("maat-adapter")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("XUBhVjBtRXlpQGNDJGZIKU5MQUVtd2NPY0FbLVN6JGg="))
                        , SignatureAlgorithm.HS256
                )
                .compact();
    }
}
