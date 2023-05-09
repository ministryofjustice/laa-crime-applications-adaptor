package uk.gov.justice.laa.crime.applications.adaptor.exception;

public class RetryableWebClientResponseException extends RuntimeException {
    public RetryableWebClientResponseException(String message) {
        super(message);
    }
}