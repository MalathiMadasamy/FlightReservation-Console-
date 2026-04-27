package com.flightreservation.util;

import com.flightreservation.model.Booking;
import com.flightreservation.model.Flight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CO3: File Handling using BufferedReader and BufferedWriter
 * Saves and loads flight/booking data from text files
 */
public class FileHandler {

    private static final String FLIGHTS_FILE  = "flights.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";

    // ===================== FLIGHTS =====================

    /**
     * Save all flights to file (overwrites the file each time)
     */
    public static void saveFlights(List<Flight> flights) {
        // CO3: BufferedWriter for writing to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FLIGHTS_FILE))) {
            for (Flight f : flights) {
                writer.write(f.toFileString()); // Write one flight per line
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save flights: " + e.getMessage());
        }
    }

    /**
     * Load all flights from file
     */
    public static List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        File file = new File(FLIGHTS_FILE);

        if (!file.exists()) return flights; // Return empty list if no file

        // CO3: BufferedReader for reading from file
        try (BufferedReader reader = new BufferedReader(new FileReader(FLIGHTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    flights.add(Flight.fromFileString(line)); // Rebuild Flight from line
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not load flights: " + e.getMessage());
        }
        return flights;
    }

    // ===================== BOOKINGS =====================

    /**
     * Append a single booking to file
     */
    public static void saveBooking(Booking booking) {
        // true = append mode (don't overwrite previous bookings)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKINGS_FILE, true))) {
            writer.write(booking.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save booking: " + e.getMessage());
        }
    }

    /**
     * Load all bookings from file
     */
    public static List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKINGS_FILE);

        if (!file.exists()) return bookings;

        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    bookings.add(Booking.fromFileString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not load bookings: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Overwrite entire bookings file (used when cancelling)
     */
    public static void saveAllBookings(List<Booking> bookings) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) {
                writer.write(b.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Could not update bookings: " + e.getMessage());
        }
    }
}
