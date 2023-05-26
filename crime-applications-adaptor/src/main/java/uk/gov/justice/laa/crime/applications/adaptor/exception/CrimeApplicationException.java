package uk.gov.justice.laa.crime.applications.adaptor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class CrimeApplicationException extends RuntimeException {

    private final HttpStatus httpResponseCode;

    public CrimeApplicationException(HttpStatus httpStatus, String message) {
        super(message);
        httpResponseCode = httpStatus;
    }
}
