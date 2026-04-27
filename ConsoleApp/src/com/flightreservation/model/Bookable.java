package com.flightreservation.model;

/**
 * CO2: Interface - defines a contract.
 * Any class that implements this MUST provide book() and cancel() methods.
 */
public interface Bookable {
    boolean book(int seats);       // Book N seats
    boolean cancel(int seats);     // Cancel N seats
    boolean isAvailable();         // Check if seats are available
}
