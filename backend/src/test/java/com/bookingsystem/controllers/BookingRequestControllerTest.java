package com.bookingsystem.controllers;

import com.bookingsystem.dto.BookingRequestDto;
import com.bookingsystem.exception.AdSpaceNotAvailableException;
import com.bookingsystem.exception.AdSpaceNotFoundException;
import com.bookingsystem.exception.BookingValidationException;
import com.bookingsystem.model.*;
import com.bookingsystem.service.BookingRequestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRequestControllerTest {

    @Mock
    private BookingRequestService bookingRequestService;

    @InjectMocks
    private BookingRequestController bookingRequestController;

    private AdSpace dummyAdSpace() {
        return new AdSpace(
                "Test Space",
                AdSpaceType.BILLBOARD,
                "Bucharest",
                "Test Street 1",
                new BigDecimal("100.00"),
                AdSpaceStatus.AVAILABLE
        );
    }

    private BookingRequest sampleBooking() {
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(7);
        return new BookingRequest(
                dummyAdSpace(),
                "John Doe",
                "john@example.com",
                start,
                end,
                new BigDecimal("700.00")
        );
    }

    @Test
    @DisplayName("""
        GIVEN a valid booking request body
        WHEN createBooking is called
        THEN the service is invoked and the created booking is returned
    """)
    void createBooking_returnsCreatedBooking() {
        // GIVEN
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(7);
        BookingRequestDto.Create request = new BookingRequestDto.Create(
                1L,
                "John Doe",
                "john@example.com",
                start,
                end
        );

        BookingRequest booking = sampleBooking();
        when(bookingRequestService.createBooking(
                1L, "John Doe", "john@example.com", start, end))
                .thenReturn(booking);

        // WHEN
        BookingRequest result = bookingRequestController.createBooking(request);

        // THEN
        assertSame(booking, result);
        verify(bookingRequestService).createBooking(
                1L, "John Doe", "john@example.com", start, end);
    }

    @Test
    @DisplayName("""
        GIVEN a booking request for a non-existent ad space
        WHEN createBooking is called
        THEN AdSpaceNotFoundException is propagated
    """)
    void createBooking_propagatesAdSpaceNotFound() {
        // GIVEN
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(7);
        BookingRequestDto.Create request = new BookingRequestDto.Create(
                99L,
                "John Doe",
                "john@example.com",
                start,
                end
        );

        when(bookingRequestService.createBooking(
                99L, "John Doe", "john@example.com", start, end))
                .thenThrow(new AdSpaceNotFoundException(99L));

        // WHEN / THEN
        assertThrows(
                AdSpaceNotFoundException.class,
                () -> bookingRequestController.createBooking(request)
        );
    }

    @Test
    @DisplayName("""
        GIVEN a booking request that violates business rules (overlap, dates, etc.)
        WHEN createBooking is called
        THEN a BookingValidationException (or related domain exception) is propagated
    """)
    void createBooking_propagatesDomainValidationErrors() {
        // GIVEN
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(7);
        BookingRequestDto.Create request = new BookingRequestDto.Create(
                1L,
                "John Doe",
                "john@example.com",
                start,
                end
        );

        when(bookingRequestService.createBooking(
                1L, "John Doe", "john@example.com", start, end))
                .thenThrow(new BookingValidationException("Overlap detected"));

        // WHEN / THEN
        assertThrows(
                BookingValidationException.class,
                () -> bookingRequestController.createBooking(request)
        );
    }

    @Test
    @DisplayName("""
        GIVEN a booking request for an ad space that is currently not AVAILABLE
        WHEN createBooking is called
        THEN AdSpaceNotAvailableException is propagated
    """)
    void createBooking_propagatesAdSpaceNotAvailable() {
        // GIVEN
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(7);
        BookingRequestDto.Create request = new BookingRequestDto.Create(
                1L,
                "John Doe",
                "john@example.com",
                start,
                end
        );

        when(bookingRequestService.createBooking(
                1L, "John Doe", "john@example.com", start, end))
                .thenThrow(new AdSpaceNotAvailableException("Ad space is not available"));

        // WHEN / THEN
        assertThrows(
                AdSpaceNotAvailableException.class,
                () -> bookingRequestController.createBooking(request)
        );
    }
}
