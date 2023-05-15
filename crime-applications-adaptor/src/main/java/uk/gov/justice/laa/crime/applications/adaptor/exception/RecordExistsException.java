package uk.gov.justice.laa.crime.applications.adaptor.exception;

public class RecordExistsException extends RuntimeException {

    public RecordExistsException(String message) {
        super(message);
    }
}
