package com.bookingsystem.exception;

public class AdSpaceNotFoundException extends RuntimeException {
    public AdSpaceNotFoundException(Long id) {
        super("Ad space not found with id: " + id);
    }
}
