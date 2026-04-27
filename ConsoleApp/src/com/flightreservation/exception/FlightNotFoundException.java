package com.flightreservation.exception;

/**
 * CO4: Custom Exception - thrown when a flight is not found
 * Extends Exception (checked exception)
 */
public class FlightNotFoundException extends Exception {
    public FlightNotFoundException(String message) {
        super(message);
    }
}
