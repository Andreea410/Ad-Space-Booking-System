package com.bookingsystem.controllers;

import com.bookingsystem.dto.BookingRequestDto;
import com.bookingsystem.model.BookingRequest;
import com.bookingsystem.model.BookingStatus;
import com.bookingsystem.service.BookingRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking-requests")
public class BookingRequestController {

    private final BookingRequestService bookingRequestService;

    public BookingRequestController(BookingRequestService bookingRequestService) {
        this.bookingRequestService = bookingRequestService;
    }

    /**
     * POST /api/v1/booking-requests
     *
     * Creates a new booking request.
     * - 201 Created on success
     * - 400 / 404 / 409 errors handled globally
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingRequest createBooking(@Valid @RequestBody BookingRequestDto.Create request) {
        return bookingRequestService.createBooking(
                request.adSpaceId(),
                request.advertiserName(),
                request.advertiserEmail(),
                request.startDate(),
                request.endDate()
        );
    }

    /**
     * GET /api/v1/booking-requests
     *
     * Lists booking requests, optionally filtered by status.
     * - 200 OK with a list of bookings
     * - 400 Bad Request if the status parameter is invalid
     */
    @GetMapping
    public List<BookingRequest> listBookings(
            @RequestParam(name = "status", required = false) String statusParam
    ) {
        BookingStatus status = null;
        if (statusParam != null && !statusParam.isBlank()) {
            try {
                status = BookingStatus.valueOf(statusParam.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid 'status' parameter. Allowed values: " +
                                Arrays.toString(BookingStatus.values())
                );
            }
        }

        return bookingRequestService.listBookings(status);
    }

    /**
     * GET /api/v1/booking-requests/{id}
     *
     * Returns booking request details.
     * - 200 OK when the booking exists
     * - 404 Not Found when the booking does not exist (handled globally)
     */
    @GetMapping("/{id}")
    public BookingRequest getBookingById(@PathVariable Long id) {
        return bookingRequestService.getBookingById(id);
    }

    /**
     * PATCH /api/v1/booking-requests/{id}/approve
     *
     * Approves a pending booking.
     * - 200 OK with the updated booking on success
     * - 400 Bad Request if the booking is not in a valid state for approval
     * - 404 Not Found if the booking does not exist
     */
    @PatchMapping("/{id}/approve")
    public BookingRequest approveBooking(@PathVariable Long id) {
        return bookingRequestService.approveBooking(id);
    }

    /**
     * PATCH /api/v1/booking-requests/{id}/reject
     *
     * Rejects a pending booking.
     * - 200 OK with the updated booking on success
     * - 400 Bad Request if the booking is not in a valid state for rejection
     * - 404 Not Found if the booking does not exist
     */
    @PatchMapping("/{id}/reject")
    public BookingRequest rejectBooking(@PathVariable Long id) {
        return bookingRequestService.rejectBooking(id);
    }
}