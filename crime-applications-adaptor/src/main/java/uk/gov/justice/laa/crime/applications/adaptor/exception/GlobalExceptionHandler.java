
package uk.gov.justice.laa.crime.applications.adaptor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {


 @ExceptionHandler(WebClientResponseException.class)
    public ProblemDetail onRuntimeException(WebClientResponseException exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ProblemDetail onRuntimeException(WebClientRequestException exception){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return problemDetail;
    }

}

