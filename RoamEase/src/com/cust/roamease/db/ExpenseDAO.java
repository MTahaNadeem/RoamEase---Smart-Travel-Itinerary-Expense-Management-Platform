package com.cust.roamease.db;

import com.cust.roamease.models.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * ExpenseDAO Class
 * Data Access Object for Expense operations.
 * Single Responsibility: All SQL queries for Expense operations are isolated
 * here.
 * 
 * Course: SE3512
 * Instructor: Ms. Nadia Ashfaq
 * Team: Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa
 */
public class ExpenseDAO {
    private final Connection connection;

    /**
     * Constructor that accepts a database connection.
     *
     * @param connection the SQL connection to use
     */
    public ExpenseDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new expense into the database.
     *
     * @param expense the Expense object to insert
     * @return the generated expense ID, or -1 if insertion fails
     */
    public int insertExpense(Expense expense) {
        String sql = "INSERT INTO expenses (trip_id, payer_id, amount, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, expense.getTripId());
            pstmt.setInt(2, expense.getPayerId());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getDescription());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int expenseId = generatedKeys.getInt(1);
                    expense.setExpenseId(expenseId);
                    return expenseId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting expense: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves an expense by ID.
     *
     * @param expenseId the expense ID to search for
     * @return the Expense object if found, null otherwise
     */
    public Expense getExpenseById(int expenseId) {
        String sql = "SELECT expense_id, trip_id, payer_id, amount, description FROM expenses WHERE expense_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Expense(
                            rs.getInt("expense_id"),
                            rs.getInt("trip_id"),
                            rs.getInt("payer_id"),
                            rs.getDouble("amount"),
                            rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving expense: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all expenses from the database.
     *
     * @return a List of all Expense objects
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT expense_id, trip_id, payer_id, amount, description FROM expenses";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("expense_id"),
                        rs.getInt("trip_id"),
                        rs.getInt("payer_id"),
                        rs.getDouble("amount"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * Retrieves all expenses for a specific trip.
     *
     * @param tripId the trip ID to filter by
     * @return a List of Expense objects for the trip
     */
    public List<Expense> getExpensesByTrip(int tripId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT expense_id, trip_id, payer_id, amount, description FROM expenses WHERE trip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(new Expense(
                            rs.getInt("expense_id"),
                            rs.getInt("trip_id"),
                            rs.getInt("payer_id"),
                            rs.getDouble("amount"),
                            rs.getString("description")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving expenses for trip: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * Calculates the total expenses for a trip.
     *
     * @param tripId the trip ID
     * @return the total amount spent
     */
    public double getTotalExpensesForTrip(int tripId) {
        String sql = "SELECT SUM(amount) as total FROM expenses WHERE trip_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total expenses: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Gets expenses grouped by payer for a trip.
     *
     * @param tripId the trip ID
     * @return a Map where key is payer ID and value is total amount paid
     */
    public Map<Integer, Double> getExpensesByPayer(int tripId) {
        Map<Integer, Double> payerExpenses = new HashMap<>();
        String sql = "SELECT payer_id, SUM(amount) as total FROM expenses WHERE trip_id = ? GROUP BY payer_id";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, tripId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payerExpenses.put(rs.getInt("payer_id"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving expenses by payer: " + e.getMessage());
        }
        return payerExpenses;
    }

    /**
     * Updates an existing expense.
     *
     * @param expense the Expense object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET trip_id = ?, payer_id = ?, amount = ?, description = ? WHERE expense_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, expense.getTripId());
            pstmt.setInt(2, expense.getPayerId());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getDescription());
            pstmt.setInt(5, expense.getExpenseId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating expense: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes an expense from the database.
     *
     * @param expenseId the expense ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE expense_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
        }
        return false;
    }
}
