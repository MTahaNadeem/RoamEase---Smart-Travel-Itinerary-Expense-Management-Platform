package com.cust.roamease.models;

/**
 * User Model Class
 * Represents a user in the RoamEase application with standard encapsulation.
 *
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class User {
    private int userId;
    private String name;
    private String email;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructor with parameters.
     *
     * @param userId the unique identifier for the user
     * @param name   the name of the user
     * @param email  the email of the user
     */
    public User(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    /**
     * Constructor without userId (for creation).
     *
     * @param name  the name of the user
     * @param email the email of the user
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
