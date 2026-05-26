package com.cust.roamease.db;

import java.sql.*;

/**
 * DatabaseManager Class
 * Manages SQLite database connection and schema initialization.
 * Singleton pattern ensures only one database connection.
 * Single Responsibility: Database connection and table creation only.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DATABASE_URL = "jdbc:sqlite:database/roamease.db";

    /**
     * Private constructor for Singleton pattern.
     */
    private DatabaseManager() {
        initializeDatabase();
    }

    /**
     * Gets the singleton instance of DatabaseManager.
     *
     * @return the DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Initializes the database and creates tables if they don't exist.
     */
    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            createTables();
            System.out.println("Database initialized successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    /**
     * Creates all required tables in the database if they don't exist.
     */
    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create Users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE" +
                    ");";
            stmt.execute(createUsersTable);

            // Create Trips table
            String createTripsTable = "CREATE TABLE IF NOT EXISTS trips (" +
                    "trip_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "destination TEXT NOT NULL," +
                    "start_date TEXT NOT NULL," +
                    "end_date TEXT NOT NULL" +
                    ");";
            stmt.execute(createTripsTable);

            // Create Itinerary Events table
            String createEventsTable = "CREATE TABLE IF NOT EXISTS itinerary_events (" +
                    "event_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "trip_id INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "datetime TEXT NOT NULL," +
                    "location TEXT NOT NULL," +
                    "FOREIGN KEY(trip_id) REFERENCES trips(trip_id) ON DELETE CASCADE" +
                    ");";
            stmt.execute(createEventsTable);

            // Create Expenses table
            String createExpensesTable = "CREATE TABLE IF NOT EXISTS expenses (" +
                    "expense_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "trip_id INTEGER NOT NULL," +
                    "payer_id INTEGER NOT NULL," +
                    "amount REAL NOT NULL," +
                    "description TEXT NOT NULL," +
                    "FOREIGN KEY(trip_id) REFERENCES trips(trip_id) ON DELETE CASCADE," +
                    "FOREIGN KEY(payer_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                    ");";
            stmt.execute(createExpensesTable);

            // Create Trip Participants table (tracks which users are in which trips)
            String createTripParticipantsTable = "CREATE TABLE IF NOT EXISTS trip_participants (" +
                    "participant_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "trip_id INTEGER NOT NULL," +
                    "user_id INTEGER NOT NULL," +
                    "FOREIGN KEY(trip_id) REFERENCES trips(trip_id) ON DELETE CASCADE," +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                    "UNIQUE(trip_id, user_id)" +
                    ");";
            stmt.execute(createTripParticipantsTable);

            System.out.println("All tables created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Gets the database connection.
     *
     * @return the SQL Connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
