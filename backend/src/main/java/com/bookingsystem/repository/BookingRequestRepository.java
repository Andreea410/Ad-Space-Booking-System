package com.bookingsystem.repository;

import com.bookingsystem.model.BookingRequest;
import com.bookingsystem.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    List<BookingRequest> findByAdSpaceIdAndStatus(Long adSpaceId, BookingStatus status);
    
    @Query("SELECT b FROM BookingRequest b JOIN FETCH b.adSpace WHERE b.status = :status")
    List<BookingRequest> findByStatus(@Param("status") BookingStatus status);
    
    @Query("SELECT b FROM BookingRequest b JOIN FETCH b.adSpace")
    List<BookingRequest> findAll();
}
