package com.flightreservation.exception;

/**
 * CO4: Custom Exception - thrown when no seats available
 */
public class NoSeatsAvailableException extends Exception {
    public NoSeatsAvailableException(String message) {
        super(message);
    }
}
