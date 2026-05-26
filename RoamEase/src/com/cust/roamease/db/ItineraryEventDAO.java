package com.cust.roamease.db;

import com.cust.roamease.models.ItineraryEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ItineraryEventDAO Class
 * Data Access Object for ItineraryEvent operations.
 * Single Responsibility: All SQL queries for ItineraryEvent operations are
 * isolated here.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class ItineraryEventDAO {
    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     *
     * @param connection the SQL connection to use
     */
    public ItineraryEventDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new itinerary event into the database.
     *
     * @param event the ItineraryEvent object to insert
     * @return the generated event ID, or -1 if insertion fails
     */
    public int insertEvent(ItineraryEvent event) {
        String sql = "INSERT INTO itinerary_events (trip_id, title, datetime, location) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, event.getTripId());
            pstmt.setString(2, event.getTitle());
            pstmt.setString(3, event.getDatetime().toString());
            pstmt.setString(4, event.getLocation());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int eventId = generatedKeys.getInt(1);
                    event.setEventId(eventId);
                    return eventId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting event: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves an event by ID.
     *
     * @param eventId the event ID to search for
     * @return the ItineraryEvent object if found, null otherwise
     */
    public ItineraryEvent getEventById(int eventId) {
        String sql = "SELECT event_id, trip_id, title, datetime, location FROM itinerary_events WHERE event_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ItineraryEvent(
                            rs.getInt("event_id"),
                            rs.getInt("trip_id"),
                            rs.getString("title"),
                            LocalDateTime.parse(rs.getString("datetime")),
                            rs.getString("location"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving event: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all events for a specific trip.
     *
     * @param tripId the trip ID
     * @return a List of ItineraryEvent objects for the trip
     */
    public List<ItineraryEvent> getEventsByTrip(int tripId) {
        List<ItineraryEvent> events = new ArrayList<>();
        String sql = "SELECT event_id, trip_id, title, datetime, location FROM itinerary_events WHERE trip_id = ? ORDER BY datetime";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    events.add(new ItineraryEvent(
                            rs.getInt("event_id"),
                            rs.getInt("trip_id"),
                            rs.getString("title"),
                            LocalDateTime.parse(rs.getString("datetime")),
                            rs.getString("location")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving events for trip: " + e.getMessage());
        }
        return events;
    }

    /**
     * Retrieves all events from the database.
     *
     * @return a List of all ItineraryEvent objects
     */
    public List<ItineraryEvent> getAllEvents() {
        List<ItineraryEvent> events = new ArrayList<>();
        String sql = "SELECT event_id, trip_id, title, datetime, location FROM itinerary_events ORDER BY datetime";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(new ItineraryEvent(
                        rs.getInt("event_id"),
                        rs.getInt("trip_id"),
                        rs.getString("title"),
                        LocalDateTime.parse(rs.getString("datetime")),
                        rs.getString("location")));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all events: " + e.getMessage());
        }
        return events;
    }

    /**
     * Updates an existing event.
     *
     * @param event the ItineraryEvent object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateEvent(ItineraryEvent event) {
        String sql = "UPDATE itinerary_events SET trip_id = ?, title = ?, datetime = ?, location = ? WHERE event_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, event.getTripId());
            pstmt.setString(2, event.getTitle());
            pstmt.setString(3, event.getDatetime().toString());
            pstmt.setString(4, event.getLocation());
            pstmt.setInt(5, event.getEventId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating event: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes an event from the database.
     *
     * @param eventId the event ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM itinerary_events WHERE event_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
        }
        return false;
    }
}
