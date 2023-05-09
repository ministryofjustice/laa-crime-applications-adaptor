package uk.gov.justice.laa.crime.applications.adaptor.exception;

public class APIClientException extends RuntimeException {
    public APIClientException(String message) {
        super(message);
    }
}
