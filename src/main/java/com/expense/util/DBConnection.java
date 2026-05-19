package com.expense.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java - Database Connection Utility
 * 
 * Single Responsibility: Provide database connections
 * - Loads MySQL JDBC driver
 * - Creates connections to expense_tracker_db
 * - Handles connection exceptions gracefully
 * 
 * @author Expense Tracker Team
 * @version 1.0
 */
public class DBConnection {
    
    // Database Configuration Constants
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_tracker_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Redeye@27";
    
    static {
        // Load JDBC Driver once when class is loaded
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Get connection to expense tracker database
     * 
     * @return Connection object connected to expense_tracker_db
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }
}
