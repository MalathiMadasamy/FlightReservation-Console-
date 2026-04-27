package com.flightreservation.model;

/**
 * CO2: Inheritance - Booking also extends TravelEntity
 * Represents a ticket booking made by a passenger
 */
public class Booking extends TravelEntity {

    private String passengerName;
    private String passengerPhone;
    private String flightNumber;
    private int seatsBooked;
    private double totalAmount;
    private String status; // "CONFIRMED" or "CANCELLED"

    /**
     * CO1: Constructor
     */
    public Booking(String bookingId, String passengerName, String passengerPhone,
                   String flightNumber, int seatsBooked, double totalAmount, String createdAt) {
        super(bookingId, createdAt);
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
        this.flightNumber = flightNumber;
        this.seatsBooked = seatsBooked;
        this.totalAmount = totalAmount;
        this.status = "CONFIRMED";
    }

    /**
     * CO2: Polymorphism - implementing abstract method from TravelEntity
     */
    @Override
    public String getDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------\n");
        sb.append("Booking ID   : ").append(id).append("\n");
        sb.append("Passenger    : ").append(passengerName).append("\n");
        sb.append("Phone        : ").append(passengerPhone).append("\n");
        sb.append("Flight No    : ").append(flightNumber).append("\n");
        sb.append("Seats Booked : ").append(seatsBooked).append("\n");
        sb.append("Total Amount : Rs. ").append(totalAmount).append("\n");
        sb.append("Status       : ").append(status).append("\n");
        sb.append("Booked On    : ").append(createdAt).append("\n");
        sb.append("----------------------------------------------");
        return sb.toString();
    }

    // Cancel this booking
    public void cancelBooking() {
        this.status = "CANCELLED";
    }

    // For saving to file
    public String toFileString() {
        return id + "," + passengerName + "," + passengerPhone + "," +
               flightNumber + "," + seatsBooked + "," + totalAmount + "," +
               status + "," + createdAt;
    }

    // Rebuild Booking from file
    public static Booking fromFileString(String line) {
        String[] parts = line.split(",");
        Booking b = new Booking(parts[0], parts[1], parts[2], parts[3],
                                Integer.parseInt(parts[4]),
                                Double.parseDouble(parts[5]), parts[7]);
        b.status = parts[6];
        return b;
    }

    // ---- Getters ----
    public String getPassengerName()  { return passengerName; }
    public String getPassengerPhone() { return passengerPhone; }
    public String getFlightNumber()   { return flightNumber; }
    public int getSeatsBooked()       { return seatsBooked; }
    public double getTotalAmount()    { return totalAmount; }
    public String getStatus()         { return status; }
}
