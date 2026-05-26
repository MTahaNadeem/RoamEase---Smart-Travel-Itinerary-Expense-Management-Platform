package com.cust.roamease.services;

import com.cust.roamease.db.*;
import com.cust.roamease.interfaces.ICalculable;
import com.cust.roamease.models.*;

import java.util.*;

/**
 * ExpenseManager Class
 * Manages expense operations and calculates splits for trips.
 * Implements ICalculable interface (demonstrating Open/Closed Principle).
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class ExpenseManager implements ICalculable {
    private final ExpenseDAO expenseDAO;
    private final UserDAO userDAO;
    private final TripDAO tripDAO;

    /**
     * Constructor with dependency injection.
     *
     * @param expenseDAO the ExpenseDAO instance
     * @param userDAO    the UserDAO instance
     * @param tripDAO    the TripDAO instance
     */
    public ExpenseManager(ExpenseDAO expenseDAO, UserDAO userDAO, TripDAO tripDAO) {
        this.expenseDAO = expenseDAO;
        this.userDAO = userDAO;
        this.tripDAO = tripDAO;
    }

    /**
     * Logs a new expense for a trip.
     *
     * @param tripId      the trip ID
     * @param payerId     the user ID of the person who paid
     * @param amount      the expense amount
     * @param description the expense description
     * @return the created Expense object, or null if creation fails
     */
    public Expense logExpense(int tripId, int payerId, double amount, String description) {
        Trip trip = tripDAO.getTripById(tripId);
        User payer = userDAO.getUserById(payerId);

        if (trip == null) {
            System.err.println("Trip not found.");
            return null;
        }
        if (payer == null) {
            System.err.println("Payer not found.");
            return null;
        }
        if (amount <= 0) {
            System.err.println("Amount must be greater than zero.");
            return null;
        }

        Expense expense = new Expense(tripId, payerId, amount, description);
        int expenseId = expenseDAO.insertExpense(expense);
        return expenseId > 0 ? expense : null;
    }

    /**
     * Calculates and displays the expense split for a trip.
     * Implements the ICalculable interface method.
     * Shows who paid what and how much each person owes.
     *
     * @param tripId the trip ID for which to calculate the split
     */
    @Override
    public void calculateSplit(int tripId) {
        Trip trip = tripDAO.getTripById(tripId);
        if (trip == null) {
            System.err.println("Trip not found.");
            return;
        }

        // Get all expenses for the trip
        List<Expense> expenses = expenseDAO.getExpensesByTrip(tripId);
        if (expenses.isEmpty()) {
            System.out.println("\nNo expenses recorded for this trip.");
            return;
        }

        // Get all participants
        List<User> participants = userDAO.getUsersByTrip(tripId);
        if (participants.isEmpty()) {
            System.out.println("\nNo participants in this trip.");
            return;
        }

        // Calculate total expenses
        double totalExpenses = 0;
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }

        // Calculate split per person
        double splitPerPerson = totalExpenses / participants.size();

        // Calculate how much each person paid and owes
        Map<Integer, Double> amountPaid = new HashMap<>();
        for (User participant : participants) {
            amountPaid.put(participant.getUserId(), 0.0);
        }

        for (Expense expense : expenses) {
            amountPaid.put(expense.getPayerId(),
                    amountPaid.get(expense.getPayerId()) + expense.getAmount());
        }

        System.out.println("\n========== Expense Split for Trip: " + trip.getDestination() + " ==========");
        System.out.println("Total Expenses: Rs. " + String.format("%.2f", totalExpenses));
        System.out.println("Number of Participants: " + participants.size());
        System.out.println("Split per Person: Rs. " + String.format("%.2f", splitPerPerson));

        System.out.println("\n--- Payment Summary ---");
        for (User participant : participants) {
            double paid = amountPaid.getOrDefault(participant.getUserId(), 0.0);
            double owes = splitPerPerson;
            double balance = paid - owes;
            System.out.println(participant.getName() + ":");
            System.out.println("  Paid: Rs. " + String.format("%.2f", paid));
            System.out.println("  Should Pay: Rs. " + String.format("%.2f", owes));
            if (balance > 0) {
                System.out.println("  Gets Back: Rs. " + String.format("%.2f", balance));
            } else if (balance < 0) {
                System.out.println("  Owes: Rs. " + String.format("%.2f", Math.abs(balance)));
            } else {
                System.out.println("  Status: Even");
            }
        }

        System.out.println("\n--- Settlements Required ---");
        List<User> debtors = new ArrayList<>();
        List<User> creditors = new ArrayList<>();

        for (User participant : participants) {
            double paid = amountPaid.getOrDefault(participant.getUserId(), 0.0);
            double owes = splitPerPerson;
            double balance = paid - owes;

            if (balance < 0) {
                debtors.add(participant);
            } else if (balance > 0) {
                creditors.add(participant);
            }
        }

        if (debtors.isEmpty() && creditors.isEmpty()) {
            System.out.println("Everyone is settled. No transactions needed.");
        } else {
            for (User creditor : creditors) {
                double creditorBalance = amountPaid.get(creditor.getUserId()) - splitPerPerson;
                for (User debtor : debtors) {
                    double debtorBalance = Math.abs(amountPaid.getOrDefault(debtor.getUserId(), 0.0) - splitPerPerson);
                    if (creditorBalance > 0 && debtorBalance > 0) {
                        double settlement = Math.min(creditorBalance, debtorBalance);
                        System.out.println(debtor.getName() + " owes " + creditor.getName() +
                                ": Rs. " + String.format("%.2f", settlement));
                        creditorBalance -= settlement;
                        debtorBalance -= settlement;
                    }
                }
            }
        }

        System.out.println("=========================================================\n");
    }

    /**
     * Retrieves all expenses for a trip.
     *
     * @param tripId the trip ID
     * @return a List of Expense objects
     */
    public List<Expense> getExpensesForTrip(int tripId) {
        return expenseDAO.getExpensesByTrip(tripId);
    }

    /**
     * Gets the total expenses for a trip.
     *
     * @param tripId the trip ID
     * @return the total amount spent
     */
    public double getTotalExpensesForTrip(int tripId) {
        return expenseDAO.getTotalExpensesForTrip(tripId);
    }
}
