package com.flightreservation.model;

/**
 * CO2: Inheritance - Flight extends TravelEntity (abstract class)
 * CO2: Implements Bookable interface (multiple inheritance via interface)
 * CO1: Class with fields, constructor, methods
 */
public class Flight extends TravelEntity implements Bookable {

    // Flight details
    private String flightNumber;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private double price;

    /**
     * CO1: Constructor - initializes all fields
     */
    public Flight(String flightNumber, String source, String destination,
                  String departureTime, String arrivalTime,
                  int totalSeats, double price, String createdAt) {
        // Call parent (TravelEntity) constructor
        super(flightNumber, createdAt);
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats; // Initially all seats are available
        this.price = price;
    }

    /**
     * CO2: Implementing abstract method from TravelEntity (Polymorphism)
     * Returns full flight info as a formatted string
     */
    @Override
    public String getDetails() {
        // CO3: StringBuilder for efficient string building
        StringBuilder sb = new StringBuilder();
        sb.append("==============================================\n");
        sb.append("Flight No : ").append(flightNumber).append("\n");
        sb.append("Route     : ").append(source).append(" --> ").append(destination).append("\n");
        sb.append("Departure : ").append(departureTime).append("\n");
        sb.append("Arrival   : ").append(arrivalTime).append("\n");
        sb.append("Seats     : ").append(availableSeats).append(" / ").append(totalSeats).append(" available\n");
        sb.append("Price     : Rs. ").append(price).append(" per seat\n");
        sb.append("==============================================");
        return sb.toString();
    }

    /**
     * CO2: Implementing book() from Bookable interface
     */
    @Override
    public boolean book(int seats) {
        if (availableSeats >= seats) {
            availableSeats -= seats;
            return true;
        }
        return false; // Not enough seats
    }

    /**
     * CO2: Implementing cancel() from Bookable interface
     */
    @Override
    public boolean cancel(int seats) {
        availableSeats += seats;
        if (availableSeats > totalSeats) {
            availableSeats = totalSeats; // Safety check
        }
        return true;
    }

    /**
     * CO2: Implementing isAvailable() from Bookable interface
     */
    @Override
    public boolean isAvailable() {
        return availableSeats > 0;
    }

    // ---- Getters ----
    public String getFlightNumber() { return flightNumber; }
    public String getSource()       { return source; }
    public String getDestination()  { return destination; }
    public String getDepartureTime(){ return departureTime; }
    public String getArrivalTime()  { return arrivalTime; }
    public int getTotalSeats()      { return totalSeats; }
    public int getAvailableSeats()  { return availableSeats; }
    public double getPrice()        { return price; }

    // Used for saving to file: "AI101,Delhi,Mumbai,10:00,12:00,100,80,4500.0,2024-01-01"
    public String toFileString() {
        return flightNumber + "," + source + "," + destination + "," +
               departureTime + "," + arrivalTime + "," +
               totalSeats + "," + availableSeats + "," + price + "," + createdAt;
    }

    /**
     * CO3: StringTokenizer used to rebuild a Flight object from a saved file line
     */
    public static Flight fromFileString(String line) {
        // Split by comma
        String[] parts = line.split(",");
        Flight f = new Flight(
            parts[0], parts[1], parts[2], parts[3], parts[4],
            Integer.parseInt(parts[5]), Double.parseDouble(parts[7]), parts[8]
        );
        f.availableSeats = Integer.parseInt(parts[6]);
        return f;
    }
}
