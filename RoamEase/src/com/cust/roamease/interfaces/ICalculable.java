package com.cust.roamease.interfaces;

/**
 * ICalculable Interface
 * Defines contract for expense calculation and splitting.
 * Demonstrates Open/Closed Principle.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public interface ICalculable {
    /**
     * Calculates and displays expense split for a given trip.
     *
     * @param tripId the ID of the trip for which to calculate the split
     */
    void calculateSplit(int tripId);
}
