package com.ecommerce.product_service.exception;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.ecommerce.com/errors/resource-not-found"));
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setProperty("resource", ex.getResourceName());
        problemDetail.setProperty("field", ex.getFieldName());
        problemDetail.setProperty("value", ex.getFieldValue());

        return problemDetail;
    }

}
