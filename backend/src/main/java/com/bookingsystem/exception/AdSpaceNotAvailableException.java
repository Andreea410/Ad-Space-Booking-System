package com.bookingsystem.exception;

public class AdSpaceNotAvailableException extends RuntimeException {
    public AdSpaceNotAvailableException(String message) {
        super(message);
    }
}