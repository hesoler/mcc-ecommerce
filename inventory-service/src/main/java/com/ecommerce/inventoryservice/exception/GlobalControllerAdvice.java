package com.ecommerce.inventoryservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {

        log.warn("Resource Not Found Exception - Path: {}, Message: {}", request.getDescription(false), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/resource-not-found"));
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setProperty("resource", ex.getResourceName());
        problemDetail.setProperty("field", ex.getFieldName());
        problemDetail.setProperty("value", ex.getFieldValue());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields."
        );

        problemDetail.setTitle("Validation Failed");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/validation-failed"));
        problemDetail.setProperty("timestamp", Instant.now().toString());

        Map<String, String> fieldErrorsMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> fieldErrorsMap.put(error.getField(), error.getDefaultMessage())
        );

        problemDetail.setProperty("fieldErrors", fieldErrorsMap);

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, WebRequest request) {

        log.error("An unexpected error {}: {}",
                request.getDescription(false), ex.getMessage(), ex);

        log.error("Unexpected error. Pls contact admin.", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred."
        );
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now().toString());
        return problemDetail;
    }

}
