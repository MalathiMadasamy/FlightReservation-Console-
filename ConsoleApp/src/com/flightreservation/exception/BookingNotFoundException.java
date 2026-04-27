package com.flightreservation.exception;

/**
 * CO4: Custom Exception - thrown when a booking ID is not found
 */
public class BookingNotFoundException extends Exception {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
