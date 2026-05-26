package com.cust.roamease;

import com.cust.roamease.ui.ConsoleApp;

/**
 * Main Entry Point for RoamEase Application
 * Smart Travel Itinerary & Expense Management Platform
 * 
 * This is a comprehensive CLI application built with:
 * - Language: Java 17+
 * - Database: SQLite with sqlite-jdbc driver
 * - Architecture: Monolithic with separation of concerns
 * - Design Patterns: DAO, Service, MVC (CLI), Singleton, Dependency Injection
 * - SOLID Principles: Single Responsibility, Open/Closed, Dependency Inversion
 * 
 * Course: SE3512 - Software Construction and Development
 * Instructor: Ms. Nadia Ashfaq1
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 * Date: 2026
 * 
 * This application allows users to:
 * - Create and manage travel trips
 * - Add itinerary events to trips
 * - Log and track expenses
 * - Calculate fair expense splits among trip participants
 * - Manage user profiles
 */
public class Main {
    /**
     * Main method - Entry point of the application.
     * Initializes and starts the ConsoleApp.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp();
        app.start();
    }
}
