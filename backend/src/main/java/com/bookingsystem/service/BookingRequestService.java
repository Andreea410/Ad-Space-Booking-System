package com.bookingsystem.service;

import com.bookingsystem.exception.AdSpaceNotAvailableException;
import com.bookingsystem.exception.AdSpaceNotFoundException;
import com.bookingsystem.exception.BookingNotFoundException;
import com.bookingsystem.exception.BookingValidationException;
import com.bookingsystem.model.AdSpace;
import com.bookingsystem.model.AdSpaceStatus;
import com.bookingsystem.model.BookingRequest;
import com.bookingsystem.model.BookingStatus;
import com.bookingsystem.repository.AdSpaceRepository;
import com.bookingsystem.repository.BookingRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BookingRequestService {

    private final BookingRequestRepository bookingRequestRepository;
    private final AdSpaceRepository adSpaceRepository;

    public BookingRequestService(BookingRequestRepository bookingRequestRepository,
                                  AdSpaceRepository adSpaceRepository) {
        this.bookingRequestRepository = bookingRequestRepository;
        this.adSpaceRepository = adSpaceRepository;
    }

    public BookingRequest createBooking(Long adSpaceId,
                                        String advertiserName,
                                        String advertiserEmail,
                                        LocalDate startDate,
                                        LocalDate endDate) {
        
        AdSpace adSpace = adSpaceRepository.findById(adSpaceId)
                .orElseThrow(() -> new AdSpaceNotFoundException(adSpaceId));

        if (adSpace.getStatus() != AdSpaceStatus.AVAILABLE) {
            throw new AdSpaceNotAvailableException(
                    "Ad space is not available. Current status: " + adSpace.getStatus());
        }

        List<BookingRequest> approvedBookings = bookingRequestRepository
                .findByAdSpaceIdAndStatus(adSpaceId, BookingStatus.APPROVED);

        boolean hasOverlap = approvedBookings.stream()
                .anyMatch(existing -> existing.overlapsWith(startDate, endDate));

        if (hasOverlap) {
            throw new BookingValidationException(
                    "There are already approved bookings for this period");
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal totalCost = adSpace.getPricePerDay()
                .multiply(BigDecimal.valueOf(daysBetween));

        BookingRequest booking = new BookingRequest(
                adSpace,
                advertiserName,
                advertiserEmail,
                startDate,
                endDate,
                totalCost
        );

        return bookingRequestRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public BookingRequest getBookingById(Long id) {
        return bookingRequestRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    public BookingRequest approveBooking(Long bookingId) {
        BookingRequest booking = bookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingValidationException(
                    "Only pending bookings can be approved. Current status: " + booking.getStatus());
        }

        List<BookingRequest> approvedBookings = bookingRequestRepository
                .findByAdSpaceIdAndStatus(booking.getAdSpace().getId(), BookingStatus.APPROVED);

        boolean hasOverlap = approvedBookings.stream()
                .anyMatch(existing -> existing.overlapsWith(booking.getStartDate(), booking.getEndDate()));

        if (hasOverlap) {
            throw new BookingValidationException(
                    "Cannot approve booking: there are already approved bookings for this period");
        }

        booking.approve();

        AdSpace adSpace = booking.getAdSpace();
        adSpace.markBooked();
        adSpaceRepository.save(adSpace);

        return bookingRequestRepository.save(booking);
    }

    public BookingRequest rejectBooking(Long bookingId) {
        BookingRequest booking = bookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingValidationException(
                    "Only pending bookings can be rejected. Current status: " + booking.getStatus());
        }

        booking.reject();
        return bookingRequestRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingRequest> listBookings(BookingStatus status) {
        if (status != null) {
            return bookingRequestRepository.findByStatus(status);
        }
        return bookingRequestRepository.findAll();
    }
}
