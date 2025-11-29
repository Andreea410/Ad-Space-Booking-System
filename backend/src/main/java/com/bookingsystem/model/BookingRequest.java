package com.bookingsystem.model;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_space_id", nullable = false)
    private AdSpace adSpace;

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
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public BookingRequest(
            AdSpace adSpace,
            String advertiserName,
            String advertiserEmail,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal totalCost,
            BookingStatus status
    ) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        this.adSpace = adSpace;
        this.advertiserName = advertiserName;
        this.advertiserEmail = advertiserEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void markPending()     { this.status = BookingStatus.PENDING; }
    public void markApproved()    { this.status = BookingStatus.APPROVED; }
    public void markRejected()    { this.status = BookingStatus.REJECTED; }
}
