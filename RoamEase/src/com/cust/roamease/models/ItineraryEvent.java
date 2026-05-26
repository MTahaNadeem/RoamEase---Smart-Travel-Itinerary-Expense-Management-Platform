package com.cust.roamease.models;

import java.time.LocalDateTime;

/**
 * ItineraryEvent Model Class
 * Represents an event within a trip itinerary.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class ItineraryEvent {
    private int eventId;
    private int tripId;
    private String title;
    private LocalDateTime datetime;
    private String location;

    /**
     * Default constructor.
     */
    public ItineraryEvent() {
    }

    /**
     * Constructor with parameters.
     *
     * @param eventId  the unique identifier for the event
     * @param tripId   the trip this event belongs to
     * @param title    the title of the event
     * @param datetime the date and time of the event
     * @param location the location of the event
     */
    public ItineraryEvent(int eventId, int tripId, String title, LocalDateTime datetime, String location) {
        this.eventId = eventId;
        this.tripId = tripId;
        this.title = title;
        this.datetime = datetime;
        this.location = location;
    }

    /**
     * Constructor without eventId (for creation).
     *
     * @param tripId   the trip this event belongs to
     * @param title    the title of the event
     * @param datetime the date and time of the event
     * @param location the location of the event
     */
    public ItineraryEvent(int tripId, String title, LocalDateTime datetime, String location) {
        this.tripId = tripId;
        this.title = title;
        this.datetime = datetime;
        this.location = location;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ItineraryEvent{" +
                "eventId=" + eventId +
                ", tripId=" + tripId +
                ", title='" + title + '\'' +
                ", datetime=" + datetime +
                ", location='" + location + '\'' +
                '}';
    }
}
