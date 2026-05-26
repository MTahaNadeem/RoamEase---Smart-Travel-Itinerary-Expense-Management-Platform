package com.cust.roamease.db;

import com.cust.roamease.models.Trip;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TripDAO Class
 * Data Access Object for Trip operations.
 * Single Responsibility: All SQL queries for Trip operations are isolated here.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class TripDAO {
    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     *
     * @param connection the SQL connection to use
     */
    public TripDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new trip into the database.
     *
     * @param trip the Trip object to insert
     * @return the generated trip ID, or -1 if insertion fails
     */
    public int insertTrip(Trip trip) {
        String sql = "INSERT INTO trips (destination, start_date, end_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, trip.getDestination());
            pstmt.setString(2, trip.getStartDate().toString());
            pstmt.setString(3, trip.getEndDate().toString());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int tripId = generatedKeys.getInt(1);
                    trip.setTripId(tripId);
                    return tripId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting trip: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves a trip by ID.
     *
     * @param tripId the trip ID to search for
     * @return the Trip object if found, null otherwise
     */
    public Trip getTripById(int tripId) {
        String sql = "SELECT trip_id, destination, start_date, end_date FROM trips WHERE trip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Trip(
                            rs.getInt("trip_id"),
                            rs.getString("destination"),
                            LocalDate.parse(rs.getString("start_date")),
                            LocalDate.parse(rs.getString("end_date")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving trip: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all trips from the database.
     *
     * @return a List of all Trip objects
     */
    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT trip_id, destination, start_date, end_date FROM trips";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trips.add(new Trip(
                        rs.getInt("trip_id"),
                        rs.getString("destination"),
                        LocalDate.parse(rs.getString("start_date")),
                        LocalDate.parse(rs.getString("end_date"))));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all trips: " + e.getMessage());
        }
        return trips;
    }

    /**
     * Updates an existing trip.
     *
     * @param trip the Trip object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateTrip(Trip trip) {
        String sql = "UPDATE trips SET destination = ?, start_date = ?, end_date = ? WHERE trip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, trip.getDestination());
            pstmt.setString(2, trip.getStartDate().toString());
            pstmt.setString(3, trip.getEndDate().toString());
            pstmt.setInt(4, trip.getTripId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating trip: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a trip from the database.
     *
     * @param tripId the trip ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteTrip(int tripId) {
        String sql = "DELETE FROM trips WHERE trip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting trip: " + e.getMessage());
        }
        return false;
    }

    /**
     * Adds a participant to a trip.
     *
     * @param tripId the trip ID
     * @param userId the user ID to add
     * @return true if successful, false otherwise
     */
    public boolean addParticipantToTrip(int tripId, int userId) {
        String sql = "INSERT OR IGNORE INTO trip_participants (trip_id, user_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding participant to trip: " + e.getMessage());
        }
        return false;
    }

    /**
     * Removes a participant from a trip.
     *
     * @param tripId the trip ID
     * @param userId the user ID to remove
     * @return true if successful, false otherwise
     */
    public boolean removeParticipantFromTrip(int tripId, int userId) {
        String sql = "DELETE FROM trip_participants WHERE trip_id = ? AND user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error removing participant from trip: " + e.getMessage());
        }
        return false;
    }

    /**
     * Gets the count of participants for a trip.
     *
     * @param tripId the trip ID
     * @return the number of participants
     */
    public int getParticipantCount(int tripId) {
        String sql = "SELECT COUNT(*) as count FROM trip_participants WHERE trip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting participant count: " + e.getMessage());
        }
        return 0;
    }
}
