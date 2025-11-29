package com.bookingsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ad_space")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @NotBlank(message = "Ad space name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Ad space type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AdSpaceType type;

    @Setter
    @NotBlank(message = "City is required")
    @Column(name = "city", nullable = false)
    private String city;

    @Setter
    @NotBlank(message = "Address is required")
    @Column(name = "address", nullable = false)
    private String address;

    @Setter
    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(name = "price_per_day", nullable = false)
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdSpaceStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AdSpace(String name,
                   AdSpaceType type,
                   String city,
                   String address,
                   BigDecimal pricePerDay,
                   AdSpaceStatus status) {

        this.name = name;
        this.type = type;
        this.city = city;
        this.address = address;
        this.pricePerDay = pricePerDay;
        this.status = status;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void changeType(AdSpaceType newType) {
        this.type = newType;
    }

    public void markAvailable() {
        this.status = AdSpaceStatus.AVAILABLE;
    }

    public void markBooked() {
        this.status = AdSpaceStatus.BOOKED;
    }

    public void markMaintenance() {
        this.status = AdSpaceStatus.MAINTENANCE;
    }
}
