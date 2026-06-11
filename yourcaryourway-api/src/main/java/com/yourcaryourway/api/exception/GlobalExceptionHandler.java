package com.yourcaryourway.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(
            ResponseStatusException exception) {

        HttpStatusCode statusCode = exception.getStatusCode();
        HttpStatus status = HttpStatus.resolve(statusCode.value());

        String error = status != null
                ? status.getReasonPhrase()
                : "Error";

        String message = exception.getReason() != null
                ? exception.getReason()
                : "Une erreur est survenue";

        ApiError apiError = new ApiError(
                statusCode.value(),
                error,
                message
        );

        return ResponseEntity
                .status(statusCode)
                .body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        ApiError apiError = new ApiError(
                500,
                "Internal Server Error",
                "Une erreur interne est survenue"
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError);
    }
}