package com.cust.roamease.services;

import com.cust.roamease.db.*;
import com.cust.roamease.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TripService Class
 * Service layer for Trip operations. Coordinates DAOs and business logic.
 * Follows Dependency Injection and Separation of Concerns principles.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class TripService {
    private final TripDAO tripDAO;
    private final UserDAO userDAO;
    private final ItineraryEventDAO eventDAO;
    private final ExpenseDAO expenseDAO;

    /**
     * Constructor with dependency injection.
     *
     * @param tripDAO    the TripDAO instance
     * @param userDAO    the UserDAO instance
     * @param eventDAO   the ItineraryEventDAO instance
     * @param expenseDAO the ExpenseDAO instance
     */
    public TripService(TripDAO tripDAO, UserDAO userDAO, ItineraryEventDAO eventDAO, ExpenseDAO expenseDAO) {
        this.tripDAO = tripDAO;
        this.userDAO = userDAO;
        this.eventDAO = eventDAO;
        this.expenseDAO = expenseDAO;
    }

    /**
     * Creates a new trip with the provided details.
     *
     * @param destination the trip destination
     * @param startDate   the start date (yyyy-MM-dd format)
     * @param endDate     the end date (yyyy-MM-dd format)
     * @return the created Trip object with generated ID, or null if creation fails
     */
    public Trip createTrip(String destination, String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            if (end.isBefore(start)) {
                System.err.println("End date must be after start date.");
                return null;
            }

            Trip trip = new Trip(destination, start, end);
            int tripId = tripDAO.insertTrip(trip);
            return tripId > 0 ? trip : null;
        } catch (Exception e) {
            System.err.println("Error creating trip: " + e.getMessage());
            return null;
        }
    }

    /**
     * Adds a participant to a trip.
     *
     * @param tripId the trip ID
     * @param userId the user ID to add
     * @return true if participant was added successfully
     */
    public boolean addParticipantToTrip(int tripId, int userId) {
        Trip trip = tripDAO.getTripById(tripId);
        User user = userDAO.getUserById(userId);

        if (trip == null) {
            System.err.println("Trip not found.");
            return false;
        }
        if (user == null) {
            System.err.println("User not found.");
            return false;
        }

        return tripDAO.addParticipantToTrip(tripId, userId);
    }

    /**
     * Adds an itinerary event to a trip.
     *
     * @param tripId   the trip ID
     * @param title    the event title
     * @param dateTime the event date/time (yyyy-MM-ddTHH:mm format)
     * @param location the event location
     * @return the created ItineraryEvent object, or null if creation fails
     */
    public ItineraryEvent addItineraryEvent(int tripId, String title, String dateTime, String location) {
        Trip trip = tripDAO.getTripById(tripId);
        if (trip == null) {
            System.err.println("Trip not found.");
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime eventDateTime = LocalDateTime.parse(dateTime, formatter);

            ItineraryEvent event = new ItineraryEvent(tripId, title, eventDateTime, location);
            int eventId = eventDAO.insertEvent(event);
            return eventId > 0 ? event : null;
        } catch (Exception e) {
            System.err.println("Error adding itinerary event: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves all trips from the database.
     *
     * @return a List of all Trip objects
     */
    public List<Trip> getAllTrips() {
        return tripDAO.getAllTrips();
    }

    /**
     * Retrieves a specific trip by ID.
     *
     * @param tripId the trip ID
     * @return the Trip object if found, null otherwise
     */
    public Trip getTripById(int tripId) {
        return tripDAO.getTripById(tripId);
    }

    /**
     * Retrieves all events for a trip.
     *
     * @param tripId the trip ID
     * @return a List of ItineraryEvent objects
     */
    public List<ItineraryEvent> getTripEvents(int tripId) {
        return eventDAO.getEventsByTrip(tripId);
    }

    /**
     * Retrieves all participants for a trip.
     *
     * @param tripId the trip ID
     * @return a List of User objects that are participants
     */
    public List<User> getTripParticipants(int tripId) {
        return userDAO.getUsersByTrip(tripId);
    }

    /**
     * Displays trip details in a formatted manner.
     *
     * @param tripId the trip ID
     */
    public void displayTripDetails(int tripId) {
        Trip trip = tripDAO.getTripById(tripId);
        if (trip == null) {
            System.out.println("Trip not found.");
            return;
        }

        System.out.println("\n========== Trip Details ==========");
        System.out.println("Trip ID: " + trip.getTripId());
        System.out.println("Destination: " + trip.getDestination());
        System.out.println("Start Date: " + trip.getStartDate());
        System.out.println("End Date: " + trip.getEndDate());

        List<User> participants = getTripParticipants(tripId);
        System.out.println("Participants: " + participants.size());
        for (User user : participants) {
            System.out.println("  - " + user.getName() + " (" + user.getEmail() + ")");
        }

        List<ItineraryEvent> events = getTripEvents(tripId);
        System.out.println("Itinerary Events: " + events.size());
        for (ItineraryEvent event : events) {
            System.out.println("  - " + event.getTitle() + " at " + event.getLocation() + " on " + event.getDatetime());
        }

        double totalExpenses = expenseDAO.getTotalExpensesForTrip(tripId);
        System.out.println("Total Expenses: Rs. " + String.format("%.2f", totalExpenses));
        System.out.println("==================================\n");
    }
}
