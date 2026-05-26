package com.cust.roamease.models;

/**
 * Expense Model Class
 * Represents an expense incurred during a trip.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class Expense {
    private int expenseId;
    private int tripId;
    private int payerId;
    private double amount;
    private String description;

    /**
     * Default constructor.
     */
    public Expense() {
    }

    /**
     * Constructor with parameters.
     *
     * @param expenseId   the unique identifier for the expense
     * @param tripId      the trip this expense belongs to
     * @param payerId     the user who paid for this expense
     * @param amount      the amount paid
     * @param description the description of the expense
     */
    public Expense(int expenseId, int tripId, int payerId, double amount, String description) {
        this.expenseId = expenseId;
        this.tripId = tripId;
        this.payerId = payerId;
        this.amount = amount;
        this.description = description;
    }

    /**
     * Constructor without expenseId (for creation).
     *
     * @param tripId      the trip this expense belongs to
     * @param payerId     the user who paid for this expense
     * @param amount      the amount paid
     * @param description the description of the expense
     */
    public Expense(int tripId, int payerId, double amount, String description) {
        this.tripId = tripId;
        this.payerId = payerId;
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters
    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", tripId=" + tripId +
                ", payerId=" + payerId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }
}
