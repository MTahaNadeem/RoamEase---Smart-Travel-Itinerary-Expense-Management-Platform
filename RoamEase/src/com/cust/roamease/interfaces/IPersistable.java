package com.cust.roamease.interfaces;

/**
 * IPersistable Interface
 * Generic interface for objects that can be saved to the database.
 * Demonstrates Open/Closed Principle and Liskov Substitution Principle.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public interface IPersistable {
    /**
     * Saves the current object to the database.
     *
     * @return true if save was successful, false otherwise
     */
    boolean save();

    /**
     * Deletes the current object from the database.
     *
     * @return true if deletion was successful, false otherwise
     */
    boolean delete();
}
