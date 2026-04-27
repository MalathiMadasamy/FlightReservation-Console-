package com.flightreservation.service;

import com.flightreservation.exception.BookingNotFoundException;
import com.flightreservation.exception.FlightNotFoundException;
import com.flightreservation.exception.NoSeatsAvailableException;
import com.flightreservation.model.Booking;
import com.flightreservation.model.Flight;
import com.flightreservation.util.FileHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * CO3: Service class - handles all business logic
 * CO3: Uses ArrayList, HashMap, HashSet (Collections Framework)
 * CO3: Uses Comparator, Iterator
 */
public class FlightService {

    // CO3: List - ordered collection of flights
    private List<Flight> flights;

    // CO3: Map - quick lookup: bookingId -> Booking
    private Map<String, Booking> bookingMap;

    // CO3: Set - tracks all unique flight numbers added
    private Set<String> flightNumberSet;

    // Counter for auto-generating booking IDs
    private int bookingCounter = 1;

    public FlightService() {
        // Load existing data from files
        flights = FileHandler.loadFlights();
        bookingMap = new LinkedHashMap<>(); // Maintains insertion order

        // Load bookings into map
        List<Booking> savedBookings = FileHandler.loadBookings();
        for (Booking b : savedBookings) {
            bookingMap.put(b.getId(), b);
            bookingCounter++;
        }

        // Build the set of known flight numbers
        flightNumberSet = new HashSet<>();
        for (Flight f : flights) {
            flightNumberSet.add(f.getFlightNumber());
        }

        // If no flights exist, add some sample flights
        if (flights.isEmpty()) {
            addSampleFlights();
        }
    }

    // Add some default flights so the app is ready to use immediately
    private void addSampleFlights() {
        String now = getCurrentDateTime();
        flights.add(new Flight("AI101", "Delhi", "Mumbai", "08:00", "10:00", 100, 4500.0, now));
        flights.add(new Flight("AI202", "Mumbai", "Kolkata", "11:00", "13:30", 80, 5200.0, now));
        flights.add(new Flight("AI303", "Chennai", "Delhi", "14:00", "16:30", 120, 4800.0, now));
        flights.add(new Flight("AI404", "Delhi", "Bangalore", "07:00", "09:15", 90, 5500.0, now));
        flights.add(new Flight("AI505", "Hyderabad", "Mumbai", "16:00", "17:45", 60, 3800.0, now));
        FileHandler.saveFlights(flights);
        for (Flight f : flights) flightNumberSet.add(f.getFlightNumber());
        System.out.println("[INFO] Sample flights loaded.");
    }

    // ==================== ADD FLIGHT ====================

    public void addFlight(String flightNumber, String source, String destination,
                          String departureTime, String arrivalTime,
                          int totalSeats, double price) throws Exception {

        // CO4: Exception if flight number already exists
        if (flightNumberSet.contains(flightNumber)) {
            throw new Exception("Flight " + flightNumber + " already exists!");
        }

        Flight flight = new Flight(flightNumber, source, destination,
                departureTime, arrivalTime, totalSeats, price, getCurrentDateTime());
        flights.add(flight);
        flightNumberSet.add(flightNumber);
        FileHandler.saveFlights(flights); // Persist to file
        System.out.println("\n✅ Flight " + flightNumber + " added successfully!");
    }

    // ==================== SEARCH FLIGHT ====================

    /**
     * Search flights by source and destination
     * CO3: Uses Comparator to sort results by price
     */
    public List<Flight> searchFlights(String source, String destination) {
        List<Flight> result = new ArrayList<>();

        // CO3: Iterator to traverse the list
        Iterator<Flight> it = flights.iterator();
        while (it.hasNext()) {
            Flight f = it.next();
            if (f.getSource().equalsIgnoreCase(source) &&
                f.getDestination().equalsIgnoreCase(destination)) {
                result.add(f);
            }
        }

        // CO3: Comparator - sort by price (ascending)
        result.sort(Comparator.comparingDouble(Flight::getPrice));
        return result;
    }

    // Get all flights
    public List<Flight> getAllFlights() {
        return flights;
    }

    // ==================== BOOK TICKET ====================

    /**
     * CO4: Throws FlightNotFoundException and NoSeatsAvailableException (custom exceptions)
     */
    public Booking bookTicket(String flightNumber, String passengerName,
                              String passengerPhone, int seats)
            throws FlightNotFoundException, NoSeatsAvailableException {

        // Find the flight
        Flight flight = findFlight(flightNumber);

        // CO4: Custom exception if flight not found
        if (flight == null) {
            throw new FlightNotFoundException("Flight " + flightNumber + " not found!");
        }

        // CO4: Custom exception if not enough seats
        if (!flight.isAvailable() || flight.getAvailableSeats() < seats) {
            throw new NoSeatsAvailableException("Not enough seats on flight " + flightNumber +
                    ". Available: " + flight.getAvailableSeats());
        }

        // Book the seats (decrements available count)
        flight.book(seats);

        // Generate unique booking ID
        String bookingId = "BK" + String.format("%04d", bookingCounter++);
        double totalAmount = seats * flight.getPrice();

        Booking booking = new Booking(bookingId, passengerName, passengerPhone,
                flightNumber, seats, totalAmount, getCurrentDateTime());

        // CO3: Store in Map for fast lookup
        bookingMap.put(bookingId, booking);

        // Save to files
        FileHandler.saveBooking(booking);
        FileHandler.saveFlights(flights);

        return booking;
    }

    // ==================== CANCEL TICKET ====================

    /**
     * CO4: Throws BookingNotFoundException
     */
    public void cancelBooking(String bookingId)
            throws BookingNotFoundException, FlightNotFoundException {

        // CO3: Map lookup by key
        Booking booking = bookingMap.get(bookingId);

        if (booking == null) {
            throw new BookingNotFoundException("Booking " + bookingId + " not found!");
        }

        if (booking.getStatus().equals("CANCELLED")) {
            System.out.println("⚠️ Booking " + bookingId + " is already cancelled.");
            return;
        }

        // Find the flight and restore seats
        Flight flight = findFlight(booking.getFlightNumber());
        if (flight == null) {
            throw new FlightNotFoundException("Associated flight not found!");
        }

        flight.cancel(booking.getSeatsBooked()); // Restore seats
        booking.cancelBooking();                 // Mark booking as cancelled

        // Save updated data
        FileHandler.saveAllBookings(new ArrayList<>(bookingMap.values()));
        FileHandler.saveFlights(flights);
        System.out.println("\n✅ Booking " + bookingId + " cancelled successfully. Seats restored.");
    }

    // ==================== VIEW BOOKINGS ====================

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookingMap.values());
    }

    public Booking getBookingById(String bookingId) {
        return bookingMap.get(bookingId);
    }

    // ==================== HELPER METHODS ====================

    // Find a flight by flight number
    private Flight findFlight(String flightNumber) {
        for (Flight f : flights) {
            if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                return f;
            }
        }
        return null;
    }

    // Get current date and time as string
    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.now().format(formatter);
    }
}
