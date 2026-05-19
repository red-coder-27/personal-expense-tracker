package com.expense.dao;

import com.expense.model.Expense;
import com.expense.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ExpenseDAO.java - Data Access Object for Expense operations
 * 
 * Single Responsibility: ALL database operations for expenses
 * - CRUD operations (Create, Read, Update, Delete)
 * - PreparedStatement usage ONLY
 * - Try-with-resources for resource management
 * - Proper SQL exception handling
 * - No SQL queries outside this class
 * 
 * @author Expense Tracker Team
 * @version 1.0
 */
public class ExpenseDAO {
    
    /**
     * Create a new expense record in database
     * 
     * JDBC Operations:
     * - PreparedStatement to prevent SQL injection
     * - Auto-generated keys retrieval
     * - Try-with-resources ensures connection closure
     * 
     * @param expense Expense object with category, description, amount, date
     * @return true if insertion successful, false otherwise
     */
    public boolean addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (category, description, amount, expense_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Bind parameters to prevent SQL injection
            pstmt.setString(1, expense.getCategory());
            pstmt.setString(2, expense.getDescription());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setDate(4, Date.valueOf(expense.getExpenseDate()));
            
            // Execute and get generated ID
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        expense.setId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieve all expenses from database
     * 
     * @return List of all Expense objects, ordered by date (newest first)
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT id, category, description, amount, expense_date FROM expenses ORDER BY expense_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Map ResultSet rows to Expense objects
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("id"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getDate("expense_date").toLocalDate()
                );
                expenses.add(expense);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all expenses: " + e.getMessage());
            e.printStackTrace();
        }
        
        return expenses;
    }
    
    /**
     * Retrieve expenses filtered by category
     * 
     * @param category Category name to filter by
     * @return List of Expense objects in specified category
     */
    public List<Expense> getExpensesByCategory(String category) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT id, category, description, amount, expense_date FROM expenses WHERE category = ? ORDER BY expense_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getDate("expense_date").toLocalDate()
                    );
                    expenses.add(expense);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving expenses by category: " + e.getMessage());
            e.printStackTrace();
        }
        
        return expenses;
    }
    
    /**
     * Retrieve single expense by ID
     * 
     * @param id Expense ID to retrieve
     * @return Expense object if found, null otherwise
     */
    public Expense getExpenseById(int id) {
        String sql = "SELECT id, category, description, amount, expense_date FROM expenses WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Expense(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getDate("expense_date").toLocalDate()
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving expense by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update existing expense record
     * 
     * @param expense Expense object with updated values (must have ID set)
     * @return true if update successful, false otherwise
     */
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET category = ?, description = ?, amount = ?, expense_date = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Bind parameters
            pstmt.setString(1, expense.getCategory());
            pstmt.setString(2, expense.getDescription());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setDate(4, Date.valueOf(expense.getExpenseDate()));
            pstmt.setInt(5, expense.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating expense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete expense record by ID
     * 
     * @param id Expense ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get total expenses amount
     * 
     * @return Sum of all expenses
     */
    public double getTotalExpenses() {
        String sql = "SELECT SUM(amount) as total FROM expenses";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating total expenses: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    /**
     * Get all distinct expense categories
     * 
     * @return List of category names
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM expenses ORDER BY category";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return categories;
    }
}
