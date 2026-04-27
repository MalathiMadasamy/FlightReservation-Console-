package com.flightreservation;

import com.flightreservation.exception.BookingNotFoundException;
import com.flightreservation.exception.FlightNotFoundException;
import com.flightreservation.exception.NoSeatsAvailableException;
import com.flightreservation.model.Booking;
import com.flightreservation.model.Flight;
import com.flightreservation.service.FlightService;
import com.flightreservation.util.BookingThread;

import java.util.List;
import java.util.Scanner;

/**
 * ============================================================
 *   FLIGHT RESERVATION SYSTEM - Console Application
 *   Covers: CO1, CO2, CO3, CO4, CO5
 * ============================================================
 *
 * CO1: Classes, Objects, Methods, Constructors
 * CO2: Inheritance, Polymorphism, Abstract Class, Interface
 * CO3: Collections, String Handling, File Handling, Exception Handling
 * CO4: Exception Handling (Custom Exceptions)
 * CO5: Multithreading
 */
public class Main {

    // Scanner for reading user input from keyboard
    static Scanner scanner = new Scanner(System.in);
    static FlightService flightService = new FlightService();

    public static void main(String[] args) {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║    ✈  FLIGHT RESERVATION SYSTEM  ✈      ║");
        System.out.println("╚══════════════════════════════════════════╝");

        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");

            // CO4: Exception handling for invalid menu input
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> addFlight();
                    case 2 -> searchFlight();
                    case 3 -> viewAllFlights();
                    case 4 -> bookTicket();
                    case 5 -> cancelTicket();
                    case 6 -> viewBookingHistory();
                    case 7 -> viewBookingById();
                    case 8 -> simulateMultithreading();
                    case 9 -> {
                        System.out.println("\n👋 Thank you for using Flight Reservation System. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("❌ Invalid choice. Please enter 1-9.");
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }

        scanner.close();
    }

    // ===================== MENU =====================
    static void printMenu() {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│          MAIN MENU               │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│ 1. Add New Flight                │");
        System.out.println("│ 2. Search Flights                │");
        System.out.println("│ 3. View All Flights              │");
        System.out.println("│ 4. Book Ticket                   │");
        System.out.println("│ 5. Cancel Booking                │");
        System.out.println("│ 6. View Booking History          │");
        System.out.println("│ 7. View Booking by ID            │");
        System.out.println("│ 8. Simulate Multithreading       │");
        System.out.println("│ 9. Exit                          │");
        System.out.println("└─────────────────────────────────┘");
    }

    // ==================== 1. ADD FLIGHT ====================
    static void addFlight() {
        System.out.println("\n--- ADD NEW FLIGHT ---");
        try {
            System.out.print("Flight Number (e.g., AI601): ");
            String flightNumber = scanner.nextLine().trim().toUpperCase();

            System.out.print("Source City: ");
            String source = scanner.nextLine().trim();

            System.out.print("Destination City: ");
            String destination = scanner.nextLine().trim();

            System.out.print("Departure Time (e.g., 09:00): ");
            String departure = scanner.nextLine().trim();

            System.out.print("Arrival Time (e.g., 11:30): ");
            String arrival = scanner.nextLine().trim();

            System.out.print("Total Seats: ");
            int seats = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Price per Seat (Rs.): ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            flightService.addFlight(flightNumber, source, destination,
                    departure, arrival, seats, price);

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number entered for seats or price!");
        } catch (Exception e) {
            // CO4: Catching custom and general exceptions
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ==================== 2. SEARCH FLIGHT ====================
    static void searchFlight() {
        System.out.println("\n--- SEARCH FLIGHTS ---");
        System.out.print("Enter Source City: ");
        String source = scanner.nextLine().trim();
        System.out.print("Enter Destination City: ");
        String destination = scanner.nextLine().trim();

        List<Flight> results = flightService.searchFlights(source, destination);

        if (results.isEmpty()) {
            System.out.println("⚠️ No flights found from " + source + " to " + destination);
        } else {
            System.out.println("\n✈ Found " + results.size() + " flight(s) (sorted by price):\n");
            for (Flight f : results) {
                System.out.println(f.getDetails()); // Polymorphism: calls Flight's getDetails()
            }
        }
    }

    // ==================== 3. VIEW ALL FLIGHTS ====================
    static void viewAllFlights() {
        List<Flight> all = flightService.getAllFlights();
        System.out.println("\n--- ALL AVAILABLE FLIGHTS (" + all.size() + ") ---\n");
        if (all.isEmpty()) {
            System.out.println("No flights available.");
        } else {
            for (Flight f : all) {
                System.out.println(f); // toString() → getDetails() (Polymorphism)
            }
        }
    }

    // ==================== 4. BOOK TICKET ====================
    static void bookTicket() {
        System.out.println("\n--- BOOK TICKET ---");
        try {
            System.out.print("Enter Flight Number: ");
            String flightNumber = scanner.nextLine().trim().toUpperCase();

            System.out.print("Passenger Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Passenger Phone: ");
            String phone = scanner.nextLine().trim();

            System.out.print("Number of Seats to Book: ");
            int seats = Integer.parseInt(scanner.nextLine().trim());

            // CO4: Catching multiple custom exceptions
            Booking booking = flightService.bookTicket(flightNumber, name, phone, seats);
            System.out.println("\n🎉 BOOKING CONFIRMED!\n" + booking.getDetails());

        } catch (FlightNotFoundException | NoSeatsAvailableException e) {
            System.out.println("❌ Booking Failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number of seats!");
        }
    }

    // ==================== 5. CANCEL TICKET ====================
    static void cancelTicket() {
        System.out.println("\n--- CANCEL BOOKING ---");
        try {
            System.out.print("Enter Booking ID (e.g., BK0001): ");
            String bookingId = scanner.nextLine().trim().toUpperCase();

            flightService.cancelBooking(bookingId);

        } catch (BookingNotFoundException | FlightNotFoundException e) {
            System.out.println("❌ Cancellation Failed: " + e.getMessage());
        }
    }

    // ==================== 6. VIEW BOOKING HISTORY ====================
    static void viewBookingHistory() {
        List<Booking> bookings = flightService.getAllBookings();
        System.out.println("\n--- BOOKING HISTORY (" + bookings.size() + " bookings) ---\n");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b.getDetails()); // Polymorphism
            }
        }
    }

    // ==================== 7. VIEW BOOKING BY ID ====================
    static void viewBookingById() {
        System.out.println("\n--- FIND BOOKING ---");
        System.out.print("Enter Booking ID: ");
        String bookingId = scanner.nextLine().trim().toUpperCase();

        Booking b = flightService.getBookingById(bookingId);
        if (b == null) {
            System.out.println("❌ Booking not found: " + bookingId);
        } else {
            System.out.println("\n" + b.getDetails());
        }
    }

    // ==================== 8. MULTITHREADING DEMO ====================
    /**
     * CO5: Demonstrates multiple threads (users) booking simultaneously
     */
    static void simulateMultithreading() {
        System.out.println("\n--- MULTITHREADING SIMULATION ---");
        System.out.println("Simulating 5 users trying to book the same flight (AI101) at the same time...\n");

        List<Flight> allFlights = flightService.getAllFlights();
        if (allFlights.isEmpty()) {
            System.out.println("No flights available for simulation.");
            return;
        }

        Flight targetFlight = allFlights.get(0); // Use first flight for demo

        // Create 5 threads (5 users)
        BookingThread user1 = new BookingThread("Aarav", targetFlight, 2);
        BookingThread user2 = new BookingThread("Priya", targetFlight, 3);
        BookingThread user3 = new BookingThread("Rohan", targetFlight, 1);
        BookingThread user4 = new BookingThread("Sneha", targetFlight, 4);
        BookingThread user5 = new BookingThread("Kiran", targetFlight, 2);

        // Start all threads simultaneously
        user1.start();
        user2.start();
        user3.start();
        user4.start();
        user5.start();

        // Wait for all threads to finish before printing result
        try {
            user1.join();
            user2.join();
            user3.join();
            user4.join();
            user5.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        System.out.println("\n[RESULT] Remaining seats on " +
                targetFlight.getFlightNumber() + ": " + targetFlight.getAvailableSeats());
    }
}
