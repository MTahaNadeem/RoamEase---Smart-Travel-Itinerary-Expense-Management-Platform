package com.cust.roamease.models;

import java.time.LocalDate;

/**
 * Trip Model Class
 * Represents a trip with destination and date information.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class Trip {
    private int tripId;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Default constructor.
     */
    public Trip() {
    }

    /**
     * Constructor with parameters.
     *
     * @param tripId      the unique identifier for the trip
     * @param destination the destination of the trip
     * @param startDate   the start date of the trip
     * @param endDate     the end date of the trip
     */
    public Trip(int tripId, String destination, LocalDate startDate, LocalDate endDate) {
        this.tripId = tripId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructor without tripId (for creation).
     *
     * @param destination the destination of the trip
     * @param startDate   the start date of the trip
     * @param endDate     the end date of the trip
     */
    public Trip(String destination, LocalDate startDate, LocalDate endDate) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", destination='" + destination + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
