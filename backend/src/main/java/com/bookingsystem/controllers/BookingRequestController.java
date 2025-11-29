package com.bookingsystem.controllers;

import com.bookingsystem.dto.BookingRequestDto;
import com.bookingsystem.model.BookingRequest;
import com.bookingsystem.service.BookingRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}