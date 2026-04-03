package com.ieltsascent.backend.api.common;

import com.ieltsascent.backend.application.auth.exception.InvalidCredentialsException;
import com.ieltsascent.backend.application.auth.exception.InvalidTokenException;
import com.ieltsascent.backend.application.auth.exception.UnauthenticatedException;
import com.ieltsascent.backend.application.auth.exception.UserAlreadyExistsException;
import com.ieltsascent.backend.application.auth.exception.UserNotFoundException;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(java.util.stream.Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        ErrorResponse response = new ErrorResponse(
            "Validation failed",
            errors,
            Instant.now().toString()
        );
        return ResponseEntity.badRequest().body(ApiResponse.failure(response));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleIllegalState(IllegalStateException ex) {
        ErrorResponse response = new ErrorResponse(
            ex.getMessage(),
            null,
            Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(response));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUserExists(UserAlreadyExistsException ex) {
        log.warn("Registration failed: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("Auth failed: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidCredentialsException.class, InvalidTokenException.class, UnauthenticatedException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAuthErrors(RuntimeException ex) {
        log.warn("Auth rejected: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResponseStatus(ResponseStatusException ex) {
        ErrorResponse response = new ErrorResponse(
            ex.getReason() != null ? ex.getReason() : ex.getMessage(),
            null,
            Instant.now().toString()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(ApiResponse.failure(response));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        ErrorResponse response = new ErrorResponse(
            "Unexpected error",
            null,
            Instant.now().toString()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure(response));
    }

    private ResponseEntity<ApiResponse<ErrorResponse>> buildErrorResponse(String message, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(
            message,
            null,
            Instant.now().toString()
        );
        return ResponseEntity.status(status).body(ApiResponse.failure(response));
    }
}
