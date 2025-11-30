package com.bookingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_space_id", nullable = false)
    private AdSpace adSpace;
    
    @JsonProperty("adSpaceId")
    public Long getAdSpaceId() {
        return adSpace != null ? adSpace.getId() : null;
    }
    
    @JsonProperty("adSpaceName")
    public String getAdSpaceName() {
        return adSpace != null ? adSpace.getName() : null;
    }

    @Setter
    @NotBlank(message = "Advertiser name is required")
    @Column(name = "advertiser_name", nullable = false)
    private String advertiserName;

    @Setter
    @NotBlank(message = "Advertiser email is required")
    @Email(message = "Invalid email format")
    @Column(name = "advertiser_email", nullable = false)
    private String advertiserEmail;

    @Setter
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Setter
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Setter
    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "booking_status default 'PENDING'")
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public BookingRequest(
            AdSpace adSpace,
            String advertiserName,
            String advertiserEmail,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal totalCost
    ) {
        validateBookingDates(startDate, endDate);
        
        this.adSpace = adSpace;
        this.advertiserName = advertiserName;
        this.advertiserEmail = advertiserEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = BookingStatus.PENDING;
    }

    private void validateBookingDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            throw new IllegalArgumentException("Start date must be in the future");
        }

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween < 7) {
            throw new IllegalArgumentException("Minimum booking duration is 7 days");
        }
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void approve() {
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be approved. Current status: " + this.status);
        }
        this.status = BookingStatus.APPROVED;
    }

    public void reject() {
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only pending bookings can be rejected. Current status: " + this.status);
        }
        this.status = BookingStatus.REJECTED;
    }

    public boolean isActive() {
        if (this.status != BookingStatus.APPROVED) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !today.isBefore(this.startDate) && !today.isAfter(this.endDate);
    }

    public boolean overlapsWith(LocalDate otherStart, LocalDate otherEnd) {
        return !this.endDate.isBefore(otherStart) && !otherEnd.isBefore(this.startDate);
    }
}
