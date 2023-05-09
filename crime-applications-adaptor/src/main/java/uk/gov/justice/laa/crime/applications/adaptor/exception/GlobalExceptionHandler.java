package uk.gov.justice.laa.crime.applications.adaptor.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String CLIENT_ERROR = "Client_Error";
    private static final String INTERNAL_SERVER_ERROR = "Internal_Server_Error";
    private static final String TIMESTAMP = "timestamp";

    @ExceptionHandler(APIClientException.class)
    ProblemDetail handleException(APIClientException e) {
        return getProblemDetail(getStatusForProblemDetail(e), CLIENT_ERROR);
    }

    @ExceptionHandler(RetryableWebClientResponseException.class)
    ProblemDetail handleException1(RetryableWebClientResponseException e) {
        return getProblemDetail(getStatusForProblemDetail(e), INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail getProblemDetail(HttpStatus httpStatus, String errorType) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, "HTTP " + httpStatus);
        problemDetail.setType(URI.create(errorType));
        problemDetail.setProperty(TIMESTAMP, Instant.now());
        return problemDetail;
    }

    private HttpStatus getStatusForProblemDetail(RuntimeException exception) {
        String statusCode = exception.getMessage();
        return getHttpStatus(statusCode);
    }

    private HttpStatus getHttpStatus(String statusCode) {
        return HttpStatus.valueOf(Integer.parseInt(statusCode));
    }
}
