package com.bookingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTOs for booking request API operations.
 * Using DTOs decouples the API contract from the JPA entity model and allows
 * us to evolve the REST interface without leaking internal fields.
 */
public class BookingRequestDto {

    public record Create(
            @NotNull Long adSpaceId,
            @NotBlank String advertiserName,
            @NotBlank @Email String advertiserEmail,
            @NotNull LocalDate startDate,
            @NotNull LocalDate endDate
    ) {}
}
