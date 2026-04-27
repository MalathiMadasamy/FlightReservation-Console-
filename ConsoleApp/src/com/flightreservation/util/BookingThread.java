package com.flightreservation.util;

import com.flightreservation.model.Flight;

/**
 * CO5: Multithreading - simulates multiple users trying to book the same flight
 * Extends Thread class (one way to create threads in Java)
 */
public class BookingThread extends Thread {

    private String userName;
    private Flight flight;
    private int seatsRequested;

    public BookingThread(String userName, Flight flight, int seatsRequested) {
        this.userName = userName;
        this.flight = flight;
        this.seatsRequested = seatsRequested;
    }

    /**
     * run() is called when thread.start() is called
     * synchronized keyword ensures only ONE thread books at a time (thread safety)
     */
    @Override
    public void run() {
        System.out.println("[THREAD] " + userName + " is trying to book " + seatsRequested + " seat(s)...");

        // synchronized on the flight object to avoid double-booking
        synchronized (flight) {
            if (flight.book(seatsRequested)) {
                System.out.println("[THREAD] ✅ " + userName + " successfully booked " + seatsRequested +
                        " seat(s) on Flight " + flight.getFlightNumber());
            } else {
                System.out.println("[THREAD] ❌ " + userName + " could NOT book - not enough seats on Flight " +
                        flight.getFlightNumber());
            }
        }
    }
}
