package com.cust.roamease.ui;

import com.cust.roamease.db.*;
import com.cust.roamease.models.*;
import com.cust.roamease.services.*;

import java.util.List;
import java.util.Scanner;

/**
 * ConsoleApp Class
 * Main CLI interface for the RoamEase application.
 * Implements the menu-driven user interface with Scanner input.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class ConsoleApp {
    private final Scanner scanner;
    private final UserDAO userDAO;
    private final TripDAO tripDAO;
    private final ExpenseDAO expenseDAO;
    private final ItineraryEventDAO eventDAO;
    private final TripService tripService;
    private final ExpenseManager expenseManager;

    /**
     * Constructor that initializes all DAOs and Services.
     */
    public ConsoleApp() {
        this.scanner = new Scanner(System.in);
        DatabaseManager dbManager = DatabaseManager.getInstance();
        this.userDAO = new UserDAO(dbManager.getConnection());
        this.tripDAO = new TripDAO(dbManager.getConnection());
        this.expenseDAO = new ExpenseDAO(dbManager.getConnection());
        this.eventDAO = new ItineraryEventDAO(dbManager.getConnection());
        this.tripService = new TripService(tripDAO, userDAO, eventDAO, expenseDAO);
        this.expenseManager = new ExpenseManager(expenseDAO, userDAO, tripDAO);
    }

    /**
     * Starts the main CLI menu loop.
     */
    public void start() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║        Welcome to RoamEase v1.0                ║");
        System.out.println("║   Smart Travel Itinerary & Expense Manager    ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            displayMainMenu();
            System.out.print("Select an option (1-6): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleCreateTrip();
                    break;
                case "2":
                    handleAddItineraryEvent();
                    break;
                case "3":
                    handleLogExpense();
                    break;
                case "4":
                    handleCalculateSplit();
                    break;
                case "5":
                    handleManageUsers();
                    break;
                case "6":
                    handleViewTrips();
                    break;
                case "7":
                    running = false;
                    System.out.println("\nThank you for using RoamEase. Goodbye!\n");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
    }

    /**
     * Displays the main menu options.
     */
    private void displayMainMenu() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("1. Create Trip");
        System.out.println("2. Add Itinerary Event");
        System.out.println("3. Log Expense");
        System.out.println("4. Calculate Expense Split");
        System.out.println("5. Manage Users");
        System.out.println("6. View Trips");
        System.out.println("7. Exit");
        System.out.println("================================\n");
    }

    /**
     * Handles the create trip workflow.
     */
    private void handleCreateTrip() {
        System.out.println("\n========== Create Trip ==========");

        // First, ensure users exist
        List<User> allUsers = userDAO.getAllUsers();
        if (allUsers.isEmpty()) {
            System.out.println("No users in the system. Please create users first.");
            return;
        }

        System.out.print("Enter trip destination: ");
        String destination = scanner.nextLine().trim();

        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDate = scanner.nextLine().trim();

        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDate = scanner.nextLine().trim();

        Trip trip = tripService.createTrip(destination, startDate, endDate);
        if (trip == null) {
            System.out.println("Failed to create trip.");
            return;
        }

        System.out.println("Trip created successfully! Trip ID: " + trip.getTripId());

        // Add participants
        boolean addingParticipants = true;
        while (addingParticipants) {
            System.out.print("Add participant? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y")) {
                System.out.println("\nAvailable Users:");
                for (User user : allUsers) {
                    System.out.println(user.getUserId() + ". " + user.getName());
                }
                System.out.print("Enter user ID: ");
                try {
                    int userId = Integer.parseInt(scanner.nextLine().trim());
                    if (tripService.addParticipantToTrip(trip.getTripId(), userId)) {
                        System.out.println("Participant added successfully.");
                    } else {
                        System.out.println("Failed to add participant.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid user ID.");
                }
            } else {
                addingParticipants = false;
            }
        }

        System.out.println("\nTrip created and configured successfully!\n");
    }

    /**
     * Handles adding itinerary events.
     */
    private void handleAddItineraryEvent() {
        System.out.println("\n========== Add Itinerary Event ==========");

        List<Trip> trips = tripService.getAllTrips();
        if (trips.isEmpty()) {
            System.out.println("No trips available. Please create a trip first.");
            return;
        }

        System.out.println("Available Trips:");
        for (Trip trip : trips) {
            System.out.println(trip.getTripId() + ". " + trip.getDestination() +
                    " (" + trip.getStartDate() + " to " + trip.getEndDate() + ")");
        }

        System.out.print("Enter trip ID: ");
        try {
            int tripId = Integer.parseInt(scanner.nextLine().trim());
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                System.out.println("Trip not found.");
                return;
            }

            System.out.print("Enter event title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter event date and time (yyyy-MM-dd'T'HH:mm): ");
            String dateTime = scanner.nextLine().trim();

            System.out.print("Enter event location: ");
            String location = scanner.nextLine().trim();

            ItineraryEvent event = tripService.addItineraryEvent(tripId, title, dateTime, location);
            if (event != null) {
                System.out.println("Event added successfully! Event ID: " + event.getEventId());
            } else {
                System.out.println("Failed to add event.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
        System.out.println();
    }

    /**
     * Handles logging expenses.
     */
    private void handleLogExpense() {
        System.out.println("\n========== Log Expense ==========");

        List<Trip> trips = tripService.getAllTrips();
        if (trips.isEmpty()) {
            System.out.println("No trips available. Please create a trip first.");
            return;
        }

        System.out.println("Available Trips:");
        for (Trip trip : trips) {
            System.out.println(trip.getTripId() + ". " + trip.getDestination());
        }

        System.out.print("Enter trip ID: ");
        try {
            int tripId = Integer.parseInt(scanner.nextLine().trim());
            Trip trip = tripService.getTripById(tripId);
            if (trip == null) {
                System.out.println("Trip not found.");
                return;
            }

            List<User> participants = tripService.getTripParticipants(tripId);
            if (participants.isEmpty()) {
                System.out.println("No participants in this trip.");
                return;
            }

            System.out.println("Participants:");
            for (User user : participants) {
                System.out.println(user.getUserId() + ". " + user.getName());
            }

            System.out.print("Enter payer ID: ");
            int payerId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter expense amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter expense description: ");
            String description = scanner.nextLine().trim();

            Expense expense = expenseManager.logExpense(tripId, payerId, amount, description);
            if (expense != null) {
                System.out.println("Expense logged successfully! Expense ID: " + expense.getExpenseId());
            } else {
                System.out.println("Failed to log expense.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
        System.out.println();
    }

    /**
     * Handles expense split calculation.
     */
    private void handleCalculateSplit() {
        System.out.println("\n========== Calculate Expense Split ==========");

        List<Trip> trips = tripService.getAllTrips();
        if (trips.isEmpty()) {
            System.out.println("No trips available.");
            return;
        }

        System.out.println("Available Trips:");
        for (Trip trip : trips) {
            System.out.println(trip.getTripId() + ". " + trip.getDestination());
        }

        System.out.print("Enter trip ID: ");
        try {
            int tripId = Integer.parseInt(scanner.nextLine().trim());
            expenseManager.calculateSplit(tripId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid trip ID.");
        }
    }

    /**
     * Handles user management.
     */
    private void handleManageUsers() {
        System.out.println("\n========== Manage Users ==========");
        System.out.println("1. Create User");
        System.out.println("2. View All Users");
        System.out.println("3. Back to Main Menu");

        System.out.print("Select option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                handleCreateUser();
                break;
            case "2":
                handleViewUsers();
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Handles user creation.
     */
    private void handleCreateUser() {
        System.out.println("\n========== Create User ==========");

        System.out.print("Enter user name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter user email: ");
        String email = scanner.nextLine().trim();

        User user = new User(name, email);
        int userId = userDAO.insertUser(user);

        if (userId > 0) {
            System.out.println("User created successfully! User ID: " + userId);
        } else {
            System.out.println("Failed to create user. Email might be duplicate.");
        }
        System.out.println();
    }

    /**
     * Handles viewing all users.
     */
    private void handleViewUsers() {
        System.out.println("\n========== All Users ==========");
        List<User> users = userDAO.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User user : users) {
                System.out.println("ID: " + user.getUserId() + " | Name: " + user.getName() +
                        " | Email: " + user.getEmail());
            }
        }
        System.out.println();
    }

    /**
     * Handles viewing trips.
     */
    private void handleViewTrips() {
        System.out.println("\n========== View Trips ==========");
        System.out.println("1. View All Trips");
        System.out.println("2. View Trip Details");
        System.out.println("3. Back to Main Menu");

        System.out.print("Select option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                handleViewAllTrips();
                break;
            case "2":
                handleViewTripDetails();
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    /**
     * Displays all trips.
     */
    private void handleViewAllTrips() {
        System.out.println("\n========== All Trips ==========");
        List<Trip> trips = tripService.getAllTrips();

        if (trips.isEmpty()) {
            System.out.println("No trips found.");
        } else {
            for (Trip trip : trips) {
                System.out.println("ID: " + trip.getTripId() + " | Destination: " + trip.getDestination() +
                        " | From: " + trip.getStartDate() + " to " + trip.getEndDate());
            }
        }
        System.out.println();
    }

    /**
     * Displays details for a specific trip.
     */
    private void handleViewTripDetails() {
        System.out.print("Enter trip ID: ");
        try {
            int tripId = Integer.parseInt(scanner.nextLine().trim());
            tripService.displayTripDetails(tripId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid trip ID.");
        }
    }
}
