package com.cust.roamease.db;

import com.cust.roamease.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO Class
 * Data Access Object for User operations.
 * Single Responsibility: All SQL queries for User operations are isolated here.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class UserDAO {
    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     *
     * @param connection the SQL connection to use
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user the User object to insert
     * @return the generated user ID, or -1 if insertion fails
     */
    public int insertUser(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    user.setUserId(userId);
                    return userId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId the user ID to search for
     * @return the User object if found, null otherwise
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, name, email FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a List of all User objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email FROM users";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Updates an existing user.
     *
     * @param user the User object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, user.getUserId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId the user ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all users for a specific trip (participants).
     *
     * @param tripId the trip ID
     * @return a List of User objects that are participants in the trip
     */
    public List<User> getUsersByTrip(int tripId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT DISTINCT u.user_id, u.name, u.email FROM users u " +
                "JOIN trip_participants tp ON u.user_id = tp.user_id " +
                "WHERE tp.trip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users for trip: " + e.getMessage());
        }
        return users;
    }
}
