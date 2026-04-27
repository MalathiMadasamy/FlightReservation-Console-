package com.flightreservation.model;

/**
 * CO2: Abstract class - cannot be instantiated directly.
 * All travel-related entities (Flight, Booking) extend this.
 */
public abstract class TravelEntity {

    // Every entity has a unique ID
    protected String id;
    protected String createdAt;

    // Constructor - sets common fields
    public TravelEntity(String id, String createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    // Abstract method - subclasses MUST implement this
    public abstract String getDetails();

    // Common getter
    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // toString uses getDetails() - Polymorphism in action
    @Override
    public String toString() {
        return getDetails();
    }
}
