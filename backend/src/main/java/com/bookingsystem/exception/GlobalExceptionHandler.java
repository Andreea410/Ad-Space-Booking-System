package com.bookingsystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global exception handler for REST controllers.
 * <p>
 * DECISION:
 * - Services throw domain-specific exceptions (e.g. BookingValidationException),
 * and this handler translates them into consistent HTTP responses.
 * - Controllers stay thin and don't duplicate error handling logic.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError buildError(HttpStatus status, String title, String message, HttpServletRequest request) {
        return new ApiError(
                status.value(),
                title,
                message,
                request.getRequestURI(),
                Instant.now()
        );
    }

    @ExceptionHandler(AdSpaceNotFoundException.class)
    public ResponseEntity<ApiError> handleAdSpaceNotFound(
            AdSpaceNotFoundException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, "Ad space not found", ex.getMessage(), request));
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiError> handleBookingNotFound(
            BookingNotFoundException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, "Booking not found", ex.getMessage(), request));
    }

    @ExceptionHandler(AdSpaceNotAvailableException.class)
    public ResponseEntity<ApiError> handleAdSpaceNotAvailable(
            AdSpaceNotAvailableException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, "Ad space not available", ex.getMessage(), request));
    }

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity<ApiError> handleBookingValidation(
            BookingValidationException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, "Booking validation failed", ex.getMessage(), request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, "Invalid request", ex.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, "Validation failed", message, request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        String message = "Cannot delete this ad space because it has associated booking requests. " +
                         "Please cancel or delete the bookings first.";
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, "Cannot delete ad space", message, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal server error",
                        "Unexpected error occurred",
                        request));
    }
}