package com.bookingsystem.service;

import com.bookingsystem.exception.AdSpaceNotAvailableException;
import com.bookingsystem.exception.AdSpaceNotFoundException;
import com.bookingsystem.exception.BookingNotFoundException;
import com.bookingsystem.exception.BookingValidationException;
import com.bookingsystem.model.*;
import com.bookingsystem.repository.AdSpaceRepository;
import com.bookingsystem.repository.BookingRequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRequestServiceTest {

    @Mock
    private BookingRequestRepository bookingRequestRepository;

    @Mock
    private AdSpaceRepository adSpaceRepository;

    @InjectMocks
    private BookingRequestService bookingRequestService;

    private AdSpace availableAdSpaceWithPrice(BigDecimal pricePerDay) {
        return new AdSpace(
                "Test Space",
                AdSpaceType.BILLBOARD,
                "Bucharest",
                "Test Street 1",
                pricePerDay,
                AdSpaceStatus.AVAILABLE
        );
    }

    @Test
    @DisplayName("""
        GIVEN a booking request for a non-existent ad space
        WHEN createBooking is invoked
        THEN AdSpaceNotFoundException is thrown
    """)
    void createBooking_throwsAdSpaceNotFound_whenAdSpaceMissing() {
        // GIVEN
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                AdSpaceNotFoundException.class,
                () -> bookingRequestService.createBooking(
                        1L,
                        "John Doe",
                        "john@example.com",
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(8)
                )
        );
    }

    @Test
    @DisplayName("""
        GIVEN a booking request for an ad space that is not AVAILABLE
        WHEN createBooking is invoked
        THEN AdSpaceNotAvailableException is thrown
    """)
    void createBooking_throwsAdSpaceNotAvailable_whenStatusNotAvailable() {
        // GIVEN
        AdSpace adSpace = availableAdSpaceWithPrice(new BigDecimal("100.00"));
        adSpace.markBooked();

        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));

        // WHEN / THEN
        assertThrows(
                AdSpaceNotAvailableException.class,
                () -> bookingRequestService.createBooking(
                        1L,
                        "John Doe",
                        "john@example.com",
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(8)
                )
        );
    }

    @Test
    @DisplayName("""
        GIVEN existing APPROVED bookings for the same ad space that overlap the requested period
        WHEN createBooking is invoked
        THEN BookingValidationException is thrown to prevent double-booking
    """)
    void createBooking_throwsBookingValidationException_whenOverlappingBookingsExist() {
        // GIVEN
        AdSpace adSpace = availableAdSpaceWithPrice(new BigDecimal("100.00"));
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));

        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(8);

        BookingRequest existing = new BookingRequest(
                adSpace,
                "Existing",
                "existing@example.com",
                start.plusDays(1),
                end.plusDays(1),
                new BigDecimal("900.00")
        );

        when(bookingRequestRepository.findByAdSpaceIdAndStatus(1L, BookingStatus.APPROVED))
                .thenReturn(List.of(existing));

        // WHEN / THEN
        assertThrows(
                BookingValidationException.class,
                () -> bookingRequestService.createBooking(
                        1L,
                        "John Doe",
                        "john@example.com",
                        start,
                        end
                )
        );
    }

    @Test
    @DisplayName("""
        GIVEN a valid booking request and AVAILABLE ad space with a known price per day
        WHEN createBooking is invoked
        THEN totalCost is calculated as pricePerDay * numberOfDays and booking is saved
    """)
    void createBooking_calculatesTotalCostAndSavesBooking() {
        // GIVEN
        BigDecimal pricePerDay = new BigDecimal("100.00");
        AdSpace adSpace = availableAdSpaceWithPrice(pricePerDay);
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));

        LocalDate start = LocalDate.now().plusDays(10);
        LocalDate end   = LocalDate.now().plusDays(20);

        when(bookingRequestRepository.findByAdSpaceIdAndStatus(1L, BookingStatus.APPROVED))
                .thenReturn(Collections.emptyList());

        ArgumentCaptor<BookingRequest> captor = ArgumentCaptor.forClass(BookingRequest.class);
        when(bookingRequestRepository.save(any(BookingRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        BookingRequest result = bookingRequestService.createBooking(
                1L,
                "John Doe",
                "john@example.com",
                start,
                end
        );

        // THEN
        verify(bookingRequestRepository).save(captor.capture());
        BookingRequest saved = captor.getValue();

        BigDecimal expectedTotal = new BigDecimal("1000.00");
        assertEquals(expectedTotal, saved.getTotalCost());
        assertEquals(expectedTotal, result.getTotalCost());
        assertEquals(BookingStatus.PENDING, result.getStatus());
        assertEquals(start, result.getStartDate());
        assertEquals(end, result.getEndDate());
    }

    @Test
    @DisplayName("""
        GIVEN an existing APPROVED booking that ends before the requested period starts
        WHEN createBooking is invoked
        THEN no overlap is detected and the new booking is saved
    """)
    void createBooking_allowsNonOverlappingBooking_afterExistingApprovedBooking() {
        // GIVEN
        AdSpace adSpace = availableAdSpaceWithPrice(new BigDecimal("100.00"));
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));

        LocalDate existingStart = LocalDate.now().plusDays(1);
        LocalDate existingEnd   = LocalDate.now().plusDays(10);
        BookingRequest existing = new BookingRequest(
                adSpace,
                "Existing",
                "existing@example.com",
                existingStart,
                existingEnd,
                new BigDecimal("900.00")
        );

        when(bookingRequestRepository.findByAdSpaceIdAndStatus(1L, BookingStatus.APPROVED))
                .thenReturn(List.of(existing));

        // new booking completely after existing
        LocalDate newStart = LocalDate.now().plusDays(15);
        LocalDate newEnd   = LocalDate.now().plusDays(25);

        when(bookingRequestRepository.save(any(BookingRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        BookingRequest result = bookingRequestService.createBooking(
                1L,
                "John Doe",
                "john@example.com",
                newStart,
                newEnd
        );

        // THEN
        assertNotNull(result);
        verify(bookingRequestRepository).save(any(BookingRequest.class));
    }

    @Test
    @DisplayName("""
        GIVEN an existing APPROVED booking that starts after the requested period ends
        WHEN createBooking is invoked
        THEN no overlap is detected and the new booking is saved
    """)
    void createBooking_allowsNonOverlappingBooking_beforeExistingApprovedBooking() {
        // GIVEN
        AdSpace adSpace = availableAdSpaceWithPrice(new BigDecimal("100.00"));
        when(adSpaceRepository.findById(1L)).thenReturn(Optional.of(adSpace));

        // existing booking in the future
        LocalDate existingStart = LocalDate.now().plusDays(30);
        LocalDate existingEnd   = LocalDate.now().plusDays(40);
        BookingRequest existing = new BookingRequest(
                adSpace,
                "Existing",
                "existing@example.com",
                existingStart,
                existingEnd,
                new BigDecimal("900.00")
        );

        when(bookingRequestRepository.findByAdSpaceIdAndStatus(1L, BookingStatus.APPROVED))
                .thenReturn(List.of(existing));

        // new booking completely before existing
        LocalDate newStart = LocalDate.now().plusDays(10);
        LocalDate newEnd   = LocalDate.now().plusDays(20);

        when(bookingRequestRepository.save(any(BookingRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        BookingRequest result = bookingRequestService.createBooking(
                1L,
                "John Doe",
                "john@example.com",
                newStart,
                newEnd
        );

        // THEN
        assertNotNull(result);
        verify(bookingRequestRepository).save(any(BookingRequest.class));
    }

    @Test
    @DisplayName("""
        GIVEN an existing booking in the repository
        WHEN getBookingById is invoked
        THEN the booking is returned
    """)
    void getBookingById_returnsBooking() {
        // GIVEN
        AdSpace adSpace = availableAdSpaceWithPrice(new BigDecimal("100.00"));
        BookingRequest booking = new BookingRequest(
                adSpace,
                "John Doe",
                "john@example.com",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(20),
                new BigDecimal("1000.00")
        );

        when(bookingRequestRepository.findById(5L)).thenReturn(Optional.of(booking));

        // WHEN
        BookingRequest result = bookingRequestService.getBookingById(5L);

        // THEN
        assertSame(booking, result);
        verify(bookingRequestRepository).findById(5L);
    }

    @Test
    @DisplayName("""
        GIVEN no booking with the requested ID exists
        WHEN getBookingById is invoked
        THEN BookingNotFoundException is thrown
    """)
    void getBookingById_throwsBookingNotFound_whenMissing() {
        // GIVEN
        when(bookingRequestRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                BookingNotFoundException.class,
                () -> bookingRequestService.getBookingById(99L)
        );
    }
}